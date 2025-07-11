package likelion13th.shop.login.service;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import likelion13th.shop.domain.User;
import likelion13th.shop.global.api.ErrorCode;
import likelion13th.shop.global.exception.GeneralException;
import likelion13th.shop.login.auth.dto.JwtDto;
import likelion13th.shop.login.auth.jwt.RefreshToken;
import likelion13th.shop.login.auth.jwt.TokenProvider;
import likelion13th.shop.login.auth.repository.RefreshTokenRepository;
import likelion13th.shop.login.auth.service.JpaUserDetailsManager;
import likelion13th.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;
    private final JpaUserDetailsManager manager;

    // ===========================================
    // ✅ 1️⃣ 회원 관련 서비스
    // ===========================================

    /**
     * ✅ provider_id(String)으로 회원 여부 확인
     */
    public Boolean checkMemberByProviderId(String providerId) {
        return userRepository.existsByProviderId(providerId);
    }

    /**
     * ✅ provider_id(String)으로 회원 찾기
     */
    // UserService.java
    public Optional<User> findByProviderId(String providerId) {
        return userRepository.findByProviderId(providerId);
    }

    /** 회원 검증용 메서드
     *  없을 경우 예외 발생 **/
    public User getAuthenticatedUser(String providerId) {
        return userRepository.findByProviderId(providerId)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND));
    }

    // ===========================================
    // ✅ 2️⃣ Refresh Token 관련 서비스
    // ===========================================

    /**
     * ✅ Refresh Token 저장 또는 갱신 (provider_id 기반)
     */
    @Transactional
    public void saveRefreshToken(String providerId, String refreshToken) {
        // 1️⃣ User 조회 (providerId 기반)
        User user = userRepository.findByProviderId(providerId)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND));

        // 2️⃣ RefreshToken 생성 또는 업데이트
        RefreshToken token = refreshTokenRepository.findByUser(user)
                .map(existingToken -> {
                    existingToken.updateRefreshToken(refreshToken);
                    return existingToken;
                })
                .orElseGet(() -> {
                    log.info("// 🆕 새 RefreshToken 생성 시도 (user_id={}, refreshToken={})",
                            user.getId(), refreshToken);
                    return RefreshToken.builder()
                            .user(user) // ✅ user와 1:1 매핑
                            .refreshToken(refreshToken)
                            .ttl(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7) // 7일 유효
                            .build();
                });

        // 3️⃣ RefreshToken 저장
        refreshTokenRepository.save(token);
        log.info("// ✅ RefreshToken 저장 완료 (user_id={})", user.getId());
    }

    // ===========================================
    // ✅ 3️⃣ JWT 생성 및 저장
    // ===========================================

    /**
     * ✅ JWT 토큰 생성 및 RefreshToken 저장
     */
    @Transactional
    public JwtDto jwtMakeSave(String providerId) {
        log.info("// UserDetailsManager 타입: {}", manager.getClass().getName());

        // 🔑 provider_id 기반 유저 인증
        UserDetails details = manager.loadUserByUsername(providerId);

        // 🔑 JWT 생성
        JwtDto jwt = tokenProvider.generateTokens(details);

        // 🔑 provider_id 기반 Refresh Token 저장
        saveRefreshToken(providerId, jwt.getRefreshToken());
        return jwt;
    }

    // ===========================================
    // ✅ 4️⃣ Refresh Token 기반 Access Token 재발급
    // ===========================================

    /**
     * ✅ Refresh Token 기반 Access Token 재발급
     */
    @Transactional
    public JwtDto reissue(HttpServletRequest request) {
        log.info("🔄 [STEP 1] Access Token 재발급 요청 시작...");

        // ✅ 1️⃣ Access Token에서 providerId 추출
        String accessToken = request.getHeader("Authorization");
        if (accessToken != null && accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.substring(7);
        }

        // ✅ 2️⃣ Access Token의 Claims 파싱 (만료된 토큰이어도 Claims 추출 가능)
        Claims claims;
        try {
            claims = tokenProvider.parseClaims(accessToken);
        } catch (Exception e) {
            log.error("❌ [ERROR] Access Token이 유효하지 않음: {}", e.getMessage());
            throw new GeneralException(ErrorCode.TOKEN_INVALID);
        }

        String providerId = claims.getSubject();
        log.info("✅ [STEP 2] Access Token에서 추출한 providerId: {}", providerId);

        if (providerId == null || providerId.isEmpty()) {
            throw new GeneralException(ErrorCode.TOKEN_INVALID);
        }

        // ✅ 3️⃣ providerId 기반으로 User 조회
        Optional<User> userOpt = findByProviderId(providerId);
        if (userOpt.isEmpty()) {
            log.error("❌ [ERROR] providerId={} 에 해당하는 사용자를 찾을 수 없음", providerId);
            throw new GeneralException(ErrorCode.USER_NOT_FOUND);
        }
        User user = userOpt.get();
        log.info("✅ [STEP 3] User 조회 성공 (user_id={}, providerId={})", user.getId(), user.getProviderId());

        // ✅ 4️⃣ DB에서 Refresh Token 조회
        RefreshToken refreshTokenEntity = refreshTokenRepository.findByUser(user)
                .orElseThrow(() -> new GeneralException(ErrorCode.WRONG_REFRESH_TOKEN));

        // ✅ 5️⃣ Refresh Token 유효성 검사
        if (!tokenProvider.validateToken(refreshTokenEntity.getRefreshToken())) {
            refreshTokenRepository.deleteByUser(user); // 만료된 Refresh Token 삭제
            log.error("❌ [ERROR] Refresh Token이 만료됨 - 삭제 완료 (user_id={})", user.getId());
            throw new GeneralException(ErrorCode.TOKEN_EXPIRED);
        }

        // ✅ 6️⃣ 새 Access Token 발급
        UserDetails userDetails = manager.loadUserByUsername(providerId);
        JwtDto newJwt = tokenProvider.generateTokens(userDetails);
        log.info("✅ [STEP 4] 새로운 Access Token 발급 완료");

        // ✅ 7️⃣ Refresh Token 갱신 (기존 토큰 삭제 후 새로운 토큰 저장)
        refreshTokenEntity.updateRefreshToken(newJwt.getRefreshToken());
        refreshTokenRepository.save(refreshTokenEntity);

        return newJwt;
    }


    // ===========================================
    // ✅ 5️⃣ 로그아웃 서비스
    // ===========================================

    /**
     * ✅ 로그아웃 (Refresh Token 삭제)
     */
    @Transactional
    public void logout(HttpServletRequest request) {
        // 1 Access Token에서 providerId 추출
        String accessToken = request.getHeader("Authorization");
        if (accessToken != null && accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.substring(7);
        }

        Claims claims = tokenProvider.parseClaims(accessToken);
        String providerId = claims.getSubject();

        if (providerId == null || providerId.isEmpty()) {
            throw new GeneralException(ErrorCode.TOKEN_INVALID);
        }

        // 2️ providerId 기반으로 User 조회
        Optional<User> userOpt = findByProviderId(providerId);
        if (userOpt.isEmpty()) {
            throw new GeneralException(ErrorCode.USER_NOT_FOUND);
        }
        User user = userOpt.get();

        // 3 Refresh Token 삭제 (DB에서 제거)
        refreshTokenRepository.deleteByUser(user);
        refreshTokenRepository.flush();
    }

}
