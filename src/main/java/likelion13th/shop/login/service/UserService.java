package likelion13th.shop.login.service;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import likelion13th.shop.domain.User;
import likelion13th.shop.global.api.ErrorCode;
import likelion13th.shop.global.exception.GeneralException;
import likelion13th.shop.login.auth.dto.JwtDto;
import likelion13th.shop.login.auth.jwt.RefreshToken;
import likelion13th.shop.login.auth.jwt.TokenProvider;
import likelion13th.shop.login.auth.repository.RefreshTokenRepository;
import likelion13th.shop.login.auth.service.JpaUserDetailsManager;
import likelion13th.shop.login.dto.UserRequestDto.UserReqDto;
import likelion13th.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

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
    public User findByProviderId(String providerId) {
        return userRepository.findByProviderId(providerId)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND));
    }

    /**
     * ✅ 신규 회원 생성 (provider_id 기반)
     */
    @Transactional
    public User createUser(UserReqDto userReqDto) {
        // 🔹 디버그 로그: 전달된 UserReqDto 정보
        log.info("// [createUser] 전달받은 UserReqDto: providerId={}, usernickname={}",
                userReqDto.getProviderId(), userReqDto.getUsernickname());

        // 🔹 User 엔티티 생성
        User newUser = User.builder()
                .providerId(userReqDto.getProviderId())   // provider_id
                .usernickname(userReqDto.getUsernickname())  // 닉네임
                .deleteable(true)                        // 기본값 true
                .mileage(0)                              // 초기 마일리지 0
                .recentTotal(0)                          // 초기 결제 금액 0
                .build();

        // 🔹 유저 저장
        User savedUser = userRepository.save(newUser);
        log.info("// 🟢 User 저장 완료: user_id={}, provider_id={}, usernickname={}",
                savedUser.getId(), savedUser.getProviderId(), savedUser.getUsernickname());

        return savedUser;
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
        // 1️⃣ Refresh Token 추출
        String refreshToken = extractRefreshToken(request);
        if (refreshToken == null) {
            throw new GeneralException(ErrorCode.TOKEN_INVALID);
        }

        // 2️⃣ 저장된 Refresh Token 확인
        RefreshToken refreshTokenEntity = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new GeneralException(ErrorCode.WRONG_REFRESH_TOKEN));

        // 3️⃣ Refresh Token 유효성 검사
        if (!tokenProvider.validateToken(refreshToken)) {
            refreshTokenRepository.deleteById(refreshTokenEntity.getId());
            throw new GeneralException(ErrorCode.TOKEN_EXPIRED);
        }

        // ✅ 4️⃣ provider_id 기반 UserDetails 로드
        String providerId = findByProviderId(refreshTokenEntity.getUser().getProviderId()).getProviderId();
        UserDetails userDetails = manager.loadUserByUsername(providerId);
        log.info("// ✅ refresh token에서 추출한 provider_id: {}", providerId);

        // ✅ 5️⃣ UserDetails 기반 Access/Refresh Token 생성
        JwtDto newJwt = tokenProvider.generateTokens(userDetails);

        // 6️⃣ Refresh Token 갱신 및 저장
        refreshTokenEntity.updateRefreshToken(newJwt.getRefreshToken());
        Claims refreshTokenClaims = tokenProvider.parseClaims(newJwt.getRefreshToken());
        Long validPeriod = refreshTokenClaims.getExpiration().getTime() - System.currentTimeMillis();
        refreshTokenEntity.updateTtl(validPeriod);
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
    public void logout(String accessToken, HttpServletResponse response) {
        Claims claims = tokenProvider.parseClaims(accessToken);
        String providerId = claims.getSubject();

        if (providerId == null || providerId.isEmpty()) {
            throw new GeneralException(ErrorCode.TOKEN_INVALID);
        }

        User user = findByProviderId(providerId);

        refreshTokenRepository.deleteByUser(user);

        boolean exists = refreshTokenRepository.findByUser(user).isPresent();
        if (exists) {
            log.warn("// ❌ RefreshToken 삭제 실패 - 여전히 DB에 존재 (user_id: {})", user.getId());
        } else {
            log.info("// 🗑️ RefreshToken 삭제 확인 완료 - DB에서 제거됨 (user_id: {})", user.getId());
        }

        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        log.info("// ✅ 로그아웃 완료 (provider_id: {})", providerId);
    }


    // ===========================================
    // ✅ 6️⃣ 현재 사용자 조회 및 유틸
    // ===========================================

    /**
     * ✅ Refresh Token 추출 (쿠키 또는 헤더)
     */
    private String extractRefreshToken(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return request.getHeader("Refresh-Token");
    }
}
