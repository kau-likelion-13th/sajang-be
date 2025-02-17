package likelion13th.shop.login.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import likelion13th.shop.global.api.ApiResponse;
import likelion13th.shop.global.api.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * ✅ JWT 유효성 검사 (provider_id 기반)
 * - 모든 요청에서 JWT 유효성을 검사해 인증 객체(SecurityContext)를 주입
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtValidationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    /**
     * ✅ 요청 시 JWT 인증 필터링
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws ServletException, IOException {
        log.info("// 🔹 요청 URL: {}", request.getServletPath());

        // ✅ 1️⃣ Authorization 헤더에서 토큰 가져오기
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("// 🔍 Authorization 헤더 값: {}", authHeader);

        // ✅ 2️⃣ 토큰이 없거나 형식이 잘못되면 필터 통과
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        // ✅ "Bearer " 접두사 제거
        String token = authHeader.substring(7);

        // ✅ 3️⃣ 토큰 유효성 검사
        if (!tokenProvider.validateToken(token)) {
            log.warn("// ❌ 유효하지 않은 토큰");
            sendErrorResponse(response, ErrorCode.TOKEN_INVALID);
            return;
        }

        try {
            // ✅ 4️⃣ Claims 추출 및 provider_id 가져오기
            var claims = tokenProvider.parseClaims(token);
            String providerId = claims.getSubject(); // subject에 provider_id 포함됨

            if (providerId == null || providerId.isEmpty()) {
                log.warn("// ❌ provider_id 추출 실패");
                sendErrorResponse(response, ErrorCode.TOKEN_INVALID);
                return;
            }

            // ✅ 5️⃣ 권한 정보 추출 및 SecurityContextHolder에 주입
            var authorities = tokenProvider.getAuthFromClaims(claims);
            // ✅ CustomUserDetails 객체로 변경
            CustomUserDetails userDetails = new CustomUserDetails(
                    providerId,
                    "",
                    authorities
            );

            // ✅ 인증 객체 생성
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(authToken);


            log.info("// 🟢 SecurityContext 주입 전: {}", SecurityContextHolder.getContext().getAuthentication());

            SecurityContextHolder.getContext().setAuthentication(authToken);

            log.info("// 🟢 SecurityContext 주입 후: {}", SecurityContextHolder.getContext().getAuthentication());
            log.info("// ✅ JWT 인증 성공 - provider_id: {}", providerId);

            // ✅ 6️⃣ 다음 필터로 요청 전달
            chain.doFilter(request, response);

        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.warn("// ❌ 잘못된 서명: {}", e.getMessage());
            sendErrorResponse(response, ErrorCode.TOKEN_INVALID);
        } catch (ExpiredJwtException e) {
            log.warn("// ❌ 토큰 만료: {}", e.getMessage());
            sendErrorResponse(response, ErrorCode.TOKEN_EXPIRED);
        } catch (UnsupportedJwtException e) {
            log.warn("// ❌ 지원되지 않는 토큰: {}", e.getMessage());
            sendErrorResponse(response, ErrorCode.TOKEN_INVALID);
        } catch (IllegalArgumentException e) {
            log.warn("// ❌ 유효하지 않은 요청: {}", e.getMessage());
            sendErrorResponse(response, ErrorCode.TOKEN_INVALID);
        } catch (Exception e) {
            log.error("// ❌ 알 수 없는 예외 발생: {}", e.getMessage());
            sendErrorResponse(response, ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * ❌ 인증 실패 시 에러 응답 반환 (401 Unauthorized)
     */
    private void sendErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(
                new ObjectMapper().writeValueAsString(ApiResponse.onFailure(errorCode, null))
        );
    }
}
