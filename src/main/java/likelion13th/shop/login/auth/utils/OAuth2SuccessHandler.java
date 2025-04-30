package likelion13th.shop.login.auth.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import likelion13th.shop.domain.Address;
import likelion13th.shop.domain.User;
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
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.List;

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
            log.info("// UserEntity address 확인: {}", newUser.getAddress().getAddress()); // ✅ Address 값이 null인지 확인
            // 🟡 2-2. Security 인증 등록
            CustomUserDetails userDetails = new CustomUserDetails(newUser);
            jpaUserDetailsManager.createUser(userDetails);
            log.info("// 신규 회원 등록 완료 (provider_id={})", providerId);
        } else {
            log.info("// ⚠️ 기존 회원 (provider_id={})", providerId);
        }

        // ✅ 3️⃣ JWT 발급 (JpaUserDetailsManager로 SecurityContext 자동 주입)
        JwtDto jwt = userService.jwtMakeSave(providerId);
        log.info("// ✅ JWT 발급 및 RefreshToken 저장 완료 (provider_id: {})", providerId);

        // 4️⃣ 프론트에서 전달한 redirect_uri 파라미터 읽기
        String frontendRedirectUri = request.getParameter("redirect_uri");
        // ▶︎ 보안 상, 미리 허용해 둔 URI 리스트에 있는지 검증
        List<String> authorizedUris = List.of(
                "https://likelionshop.netlify.app",
                "http://localhost:3000"
        );
        if (frontendRedirectUri == null || !authorizedUris.contains(frontendRedirectUri)) {
            frontendRedirectUri = "https://likelionshop.netlify.app"; // 기본값
        }

        // accessToken 쿼리 파라미터로 붙여서 리다이렉트
        String redirectUrl = UriComponentsBuilder
                .fromUriString(frontendRedirectUri)
                .queryParam("accessToken", jwt.getAccessToken())
                .build()
                .toUriString();

        log.info("// 🔄 Redirecting to Frontend: {}", redirectUrl);
        response.sendRedirect(redirectUrl);

    }
}
