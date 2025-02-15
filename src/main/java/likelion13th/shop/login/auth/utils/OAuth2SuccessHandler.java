package likelion13th.shop.login.auth.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import likelion13th.shop.global.api.ApiResponse;
import likelion13th.shop.global.api.SuccessCode;
import likelion13th.shop.global.exception.GeneralException;
import likelion13th.shop.login.auth.dto.JwtDto;
import likelion13th.shop.login.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
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

    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {
        // ✅ 1️⃣ OAuth2User 정보 추출
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String providerId = oAuth2User.getAttribute("id").toString(); // provider_id
        String nickname = oAuth2User.getAttribute("properties") != null ?
                (String) ((java.util.Map<?, ?>) oAuth2User.getAttribute("properties")).get("nickname") :
                "카카오사용자";

        log.info("// 🟢 OAuth2 Success: provider_id={}, nickname={}", providerId, nickname);

        try {
            // ✅ 2️⃣ JWT 생성 및 RefreshToken 저장
            JwtDto jwt = userService.jwtMakeSave(providerId);
            log.info("// ✅ JWT 발급 완료 (provider_id: {})", providerId);

            // ✅ 3️⃣ JSON 형태로 응답 반환
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(new ObjectMapper().writeValueAsString(
                    ApiResponse.onSuccess(SuccessCode.USER_LOGIN_SUCCESS, jwt)
            ));

        } catch (GeneralException e) {
            log.error("// ❌ OAuth2 Success 처리 중 비즈니스 에러 발생: {}", e.getReason().getMessage());
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getReason().getMessage());
        } catch (Exception e) {
            log.error("// ❌ OAuth2 Success 처리 중 예상치 못한 에러 발생: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "서버 내부 오류");
        }
    }
}
