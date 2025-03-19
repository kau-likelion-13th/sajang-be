package likelion13th.shop.controller;

import io.swagger.v3.oas.annotations.Operation;
import likelion13th.shop.DTO.response.UserInfoResponse;
import likelion13th.shop.domain.User;
import likelion13th.shop.global.api.ApiResponse;
import likelion13th.shop.global.api.ErrorCode;
import likelion13th.shop.global.api.SuccessCode;
import likelion13th.shop.global.exception.GeneralException;
import likelion13th.shop.login.auth.jwt.CustomUserDetails;
import likelion13th.shop.login.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/profile")
@RequiredArgsConstructor
public class UserInfoController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "사용자 정보 조회", description = "로그인한 사용자의 정보와 주문 상태별 개수를 조회합니다.")
    public ApiResponse<?> getUserInfo(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        User user = userService.findByProviderId(customUserDetails.getProviderId())
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND));

        UserInfoResponse userInfo = UserInfoResponse.from(user);

        return ApiResponse.onSuccess(
                SuccessCode.USER_INFO_GET_SUCCESS,
                userInfo
        );
    }
}