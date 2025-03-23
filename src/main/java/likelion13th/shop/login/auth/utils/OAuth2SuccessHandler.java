package likelion13th.shop.login.auth.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import likelion13th.shop.domain.Address;
import likelion13th.shop.domain.User;
import likelion13th.shop.global.api.ApiResponse;
import likelion13th.shop.global.api.SuccessCode;
import likelion13th.shop.login.auth.dto.JwtDto;
import likelion13th.shop.login.auth.jwt.CustomUserDetails;
import likelion13th.shop.login.auth.service.JpaUserDetailsManager;
import likelion13th.shop.login.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * ✅ OAuth2 로그인 성공 시 처리 핸들러
 * - provider_id 기반 회원 확인 및 JWT 발급
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JpaUserDetailsManager jpaUserDetailsManager;
    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {
        // ✅ 1️⃣ OAuth2User 정보 추출
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
        String providerId = (String) oAuth2User.getAttribute("provider_id");
        String nickname = (String) oAuth2User.getAttribute("nickname");

        log.info("// 🟢 OAuth2 Success: provider_id={}, nickname={}", providerId, nickname);

        // ✅ 2️⃣ 신규 회원 등록 (Security 인증 등록)
        if (!jpaUserDetailsManager.userExists(providerId)) {
            // 🟡 2-1. User 엔티티 생성
            User newUser = User.builder()
                    .providerId(providerId)
                    .usernickname(nickname)
                    .deletable(true)
                    .build();
            newUser.setAddress(new Address("10540", "경기도 고양시 덕양구 항공대학로 76", "한국항공대학교"));
            log.info("// ✅ UserEntity address 확인: {}", newUser.getAddress().getAddress()); // ✅ Address 값이 null인지 확인
            // 🟡 2-2. Security 인증 등록
            CustomUserDetails userDetails = new CustomUserDetails(newUser);
            jpaUserDetailsManager.createUser(userDetails);
            log.info("// ✅ 신규 회원 등록 완료 (provider_id={})", providerId);
        } else {
            log.info("// ⚠️ 기존 회원 (provider_id={})", providerId);
        }

        // ✅ 3️⃣ JWT 발급 (JpaUserDetailsManager로 SecurityContext 자동 주입)
        JwtDto jwt = userService.jwtMakeSave(providerId);
        log.info("// ✅ JWT 발급 및 RefreshToken 저장 완료 (provider_id: {})", providerId);

        // ✅ 4️⃣ 프론트엔드로 리다이렉트 (Query Parameter로 JWT 전달)
        String redirectUrl = String.format(
                "https://likelionshop.netlify.app/?accessToken=%s",
                jwt.getAccessToken()
        );

        log.info("// 🔄 Redirecting to Frontend: {}", redirectUrl);
        response.sendRedirect(redirectUrl);

    }
}
