package likelion13th.shop.controller;

import io.swagger.v3.oas.annotations.Operation;
import likelion13th.shop.DTO.request.AddressRequest;
import likelion13th.shop.DTO.response.AddressResponse;
import likelion13th.shop.DTO.response.UserInfoResponse;
import likelion13th.shop.DTO.response.UserMileageResponse;
import likelion13th.shop.domain.User;
import likelion13th.shop.global.api.ApiResponse;
import likelion13th.shop.global.api.ErrorCode;
import likelion13th.shop.global.api.SuccessCode;
import likelion13th.shop.global.exception.GeneralException;
import likelion13th.shop.login.auth.jwt.CustomUserDetails;
import likelion13th.shop.login.service.UserService;
import likelion13th.shop.service.UserAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/** 사용자 정보 조회, 주소 저장, 사용 가능 마일리지 조회**/
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserInfoController {

    private final UserService userService;
    private final UserAddressService userAddressService;

    @GetMapping("/profile")
    @Operation(summary = "사용자 정보 조회", description = "로그인한 사용자의 정보와 주문 상태별 개수를 조회합니다.")
    public ApiResponse<?> getUserInfo(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        User user = userService.findByProviderId(customUserDetails.getProviderId())
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND));

        UserInfoResponse userInfo = UserInfoResponse.from(user);

        return ApiResponse.onSuccess(SuccessCode.USER_INFO_GET_SUCCESS, userInfo);
    }

    // 주소 저장 API 형식 수정
    @PostMapping("/address")
    public ApiResponse<AddressResponse> saveAddress(
            @RequestBody AddressRequest request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        AddressResponse addressResponse = userAddressService.saveAddress(customUserDetails.getProviderId(), request);
        return ApiResponse.onSuccess(SuccessCode.ADDRESS_SAVE_SUCCESS, addressResponse);
    }

    // 로그인한 사용자의 사용 가능 마일리지 조회
    @GetMapping("/mileage")
    @Operation(summary = "사용 가능 마일리지 조회", description = "로그인한 사용자의 사용 가능 마일리지를 조회합니다.")
    public ApiResponse<UserMileageResponse> getAvailableMileage(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        // 로그인한 사용자 정보 조회
        User user = userService.findByProviderId(customUserDetails.getProviderId())
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND));

        // 사용 가능한 마일리지 반환
        return ApiResponse.onSuccess(SuccessCode.USER_MILEAGE_SUCCESS, new UserMileageResponse(user.getMaxMileage()));
    }
}