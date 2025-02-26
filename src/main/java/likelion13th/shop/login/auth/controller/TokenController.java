package likelion13th.shop.login.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import likelion13th.shop.global.api.ApiResponse;
import likelion13th.shop.global.api.ErrorCode;
import likelion13th.shop.global.api.SuccessCode;
import likelion13th.shop.global.exception.GeneralException;
import likelion13th.shop.login.auth.dto.JwtDto;
import likelion13th.shop.login.dto.UserRequestDto.UserReqDto;
import likelion13th.shop.login.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Tag(name = "토큰", description = "Access Token 및 Refresh Token 관련 API")
@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
public class TokenController {

    private final UserService userService;

    // ==========================================
    // ✅ 1️⃣ 토큰 생성 (회원가입 & 로그인)
    // ==========================================

    /**
     * ✅ 토큰 생성 (회원가입 & 로그인)
     * - provider_id 기반으로 JWT Access/Refresh 토큰 반환
     */
    @Operation(
            summary = "토큰 생성 (회원가입 & 로그인)",
            description = "provider_id 기반으로 JWT Access/Refresh 토큰을 반환합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER_2011", description = "회원가입 & 로그인 성공")
    })
    @PostMapping("/generate")
    public ApiResponse<JwtDto> generateToken(@RequestBody UserReqDto userReqDto) {
        try {
            String providerId = userReqDto.getProviderId();
            log.info("// [generateToken] 요청받은 providerId: {}", providerId);

            // ✅ JWT 생성 (AccessToken + RefreshToken)
            JwtDto jwt = userService.jwtMakeSave(providerId);
            log.info("// ✅ 토큰 발급 성공 (providerId: {})", providerId);

            // ✅ 이제 Refresh Token을 JSON 응답으로 직접 반환
            return ApiResponse.onSuccess(SuccessCode.USER_LOGIN_SUCCESS, jwt);
        } catch (GeneralException e) {
            log.error("// ❌ 회원가입/로그인 중 에러 발생: {}", e.getReason().getMessage());
            throw e;
        } catch (Exception e) {
            log.error("// ❌ 예상치 못한 에러 발생: {}", e.getMessage());
            throw new GeneralException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }


    // ==========================================
    // ✅ 2️⃣ 로컬 테스트용 토큰 반환 API
    // ==========================================

    /**
     * ✅ 로컬 테스트용 토큰 반환
     * - Access Token과 Refresh Token 확인용
     */
    @Operation(
            summary = "로컬 테스트용 토큰 반환",
            description = "Access Token과 Refresh Token을 확인합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON_200", description = "토큰 반환 성공")
    })
    @GetMapping("/local")
    public ApiResponse<Map<String, String>> getTokenInfo(
            @RequestParam(name = "access-token") String accessToken,
            @RequestParam(name = "refresh-token") String refreshToken
    ) {
        // ✅ 로컬 환경 테스트용 응답 구성
        Map<String, String> responseData = new HashMap<>();
        responseData.put("accessToken", accessToken);
        responseData.put("refreshToken", refreshToken);
        log.info("// ✅ 로컬 테스트용 토큰 반환 완료");
        return ApiResponse.onSuccess(SuccessCode.OK, responseData);
    }
}
