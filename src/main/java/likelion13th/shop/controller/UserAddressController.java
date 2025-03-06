package likelion13th.shop.controller;

import likelion13th.shop.DTO.request.AddressRequest;
import likelion13th.shop.DTO.response.AddressResponse;
import likelion13th.shop.global.api.ApiResponse;
import likelion13th.shop.global.api.SuccessCode;
import likelion13th.shop.login.auth.jwt.CustomUserDetails;
import likelion13th.shop.service.UserAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/address")
@RequiredArgsConstructor
public class UserAddressController {

    private final UserAddressService userAddressService;

    // 주소 저장 API 형식 수정
    @PostMapping
    public ApiResponse<AddressResponse> saveAddress(
            @RequestBody AddressRequest request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        AddressResponse addressResponse = userAddressService.saveAddress(customUserDetails.getProviderId(), request);
        return ApiResponse.onSuccess(SuccessCode.ADDRESS_SAVE_SUCCESS, addressResponse);
    }

    // 주소 조회 API 형식 수정
    @GetMapping
    public ApiResponse<AddressResponse> getAddress(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        AddressResponse addressResponse = userAddressService.getAddress(customUserDetails.getProviderId());
        return ApiResponse.onSuccess(SuccessCode.ADDRESS_GET_SUCCESS, addressResponse);
    }
}