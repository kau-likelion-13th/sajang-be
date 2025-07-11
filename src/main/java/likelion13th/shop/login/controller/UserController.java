package likelion13th.shop.login.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import likelion13th.shop.DTO.response.UserMileageResponse;
import likelion13th.shop.domain.User;
import likelion13th.shop.global.api.ApiResponse;
import likelion13th.shop.global.api.ErrorCode;
import likelion13th.shop.global.api.SuccessCode;
import likelion13th.shop.global.exception.GeneralException;
import likelion13th.shop.login.auth.dto.JwtDto;
import likelion13th.shop.login.auth.jwt.CustomUserDetails;
import likelion13th.shop.login.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "회원", description = "회원 관련 API (토큰 재발급, 로그아웃) 입니다.")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    // ✅ 토큰 재발급 API
    @Operation(summary = "토큰 재발급", description = "Refresh Token을 이용해 새로운 Access Token을 발급하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "토큰 재발급 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "유효하지 않은 토큰")
    })
    @PostMapping("/reissue")
    public ApiResponse<JwtDto> reissue(HttpServletRequest request) {
        log.info("🔄 [STEP 1] 토큰 재발급 요청 수신...");

        try {
            JwtDto jwt = userService.reissue(request);
            log.info("✅ [STEP 2] 토큰 재발급 성공 - 새로운 AccessToken 반환");
            return ApiResponse.onSuccess(SuccessCode.USER_REISSUE_SUCCESS, jwt);
        } catch (GeneralException e) {
            log.error("❌ [ERROR] 토큰 재발급 중 예외 발생: {}", e.getReason().getMessage());
            throw e;
        } catch (Exception e) {
            log.error("❌ [ERROR] 예상치 못한 예외 발생: {}", e.getMessage());
            throw new GeneralException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }


    // ✅ 로그아웃 API (Access Token을 Service에서 직접 추출하도록 변경)
    @Operation(summary = "로그아웃", description = "사용자를 로그아웃 처리하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "로그아웃 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    @DeleteMapping("/logout")
    public ApiResponse<Void> logout(HttpServletRequest request) {
        userService.logout(request);

        return ApiResponse.onSuccess(SuccessCode.USER_LOGOUT_SUCCESS, null);
    }
}


