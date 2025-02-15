package likelion13th.shop.login.auth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import likelion13th.shop.global.api.ErrorCode;
import likelion13th.shop.global.exception.GeneralException;
import likelion13th.shop.login.auth.dto.JwtDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider {

    private final Key secretKey;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    // ✅ 생성자: 설정 파일에서 키와 만료 시간 주입
    public TokenProvider(
            @Value("${JWT_SECRET}") String secretKey,
            @Value("${JWT_EXPIRATION}") long accessTokenExpiration,
            @Value("${JWT_REFRESH_EXPIRATION}") long refreshTokenExpiration) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    /**
     * ✅ AccessToken 및 RefreshToken 동시 생성
     * - UserDetails에서 userId 및 권한 추출
     */
    public JwtDto generateTokens(UserDetails userDetails) {
        log.info("JWT 생성 시작: 사용자 {}", userDetails.getUsername());
        String userId = userDetails.getUsername(); // providerId
        String authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        // Access Token 생성
        String accessToken = createToken(userId, authorities, accessTokenExpiration);
        // Refresh Token 생성 (권한은 필요 없음)
        String refreshToken = createToken(userId, null, refreshTokenExpiration);

        log.info("✅ AccessToken 및 RefreshToken 생성 완료 (userId: {})", userId);


        log.info("// 🆕 액세스 토큰 (accessToken: {})", accessToken);
        log.info("// 🆕 리프레시 토큰 (refreshToken: {})", accessToken);

        return new JwtDto(accessToken, refreshToken);
    }

    /**
     * ✅ 공통적인 JWT 생성 로직
     * @param providerId 사용자 식별자 (provider_id)
     * @param authorities 사용자 권한
     * @param expirationTime 유효기간
     */
    private String createToken(String providerId, String authorities, long expirationTime) {
        JwtBuilder jwtBuilder = Jwts.builder()
                .setSubject(providerId)                    // 사용자 식별자 (provider_id)
                .setIssuedAt(new Date())                   // 발급 시간
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime)) // 만료 시간
                .signWith(secretKey, SignatureAlgorithm.HS256); // 비밀키 및 알고리즘

        if (authorities != null) {
            jwtBuilder.claim("authorities", authorities); // 권한 포함 (AccessToken에만)
        }

        return jwtBuilder.compact();
    }

    /**
     * ✅ 토큰 검증
     * @return 유효하면 true, 그렇지 않으면 false
     */
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException e) {
            log.warn("❌ JWT 검증 실패: {}", e.getMessage());
            return false;
        }
    }

    /**
     * ✅ 토큰에서 Claims 추출
     * - AccessToken 및 RefreshToken 파싱
     */
    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.warn("❌ 토큰 만료: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.warn("❌ JWT 파싱 실패: {}", e.getMessage());
            throw new GeneralException(ErrorCode.TOKEN_INVALID);
        }
    }

    /**
     * ✅ Claims -> 권한 정보 추출 (Spring Security 호환)
     */
    public Collection<? extends GrantedAuthority> getAuthFromClaims(Claims claims) {
        String authoritiesString = claims.get("authorities", String.class);
        if (authoritiesString == null || authoritiesString.isEmpty()) {
            log.warn("// ⚠️ 권한 정보 없음 - 기본 ROLE_USER 부여");
            return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        }

        return Arrays.stream(authoritiesString.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
