package likelion13th.shop.controller;

import io.swagger.v3.oas.annotations.Operation;
import likelion13th.shop.DTO.response.OrderResponseDto;
import likelion13th.shop.DTO.response.UserInfoResponse;
import likelion13th.shop.domain.User;
import likelion13th.shop.global.api.ApiResponse;
import likelion13th.shop.global.api.ErrorCode;
import likelion13th.shop.global.api.SuccessCode;
import likelion13th.shop.global.constant.OrderStatus;
import likelion13th.shop.global.exception.GeneralException;
import likelion13th.shop.login.auth.jwt.CustomUserDetails;
import likelion13th.shop.login.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users/profile")
@RequiredArgsConstructor
public class UserInfoController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "사용자 정보 조회", description = "로그인한 사용자의 정보를 조회합니다.")
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

    @GetMapping("/orders/status/{status}")
    @Operation(summary = "특정 상태의 주문 목록 조회", description = "로그인한 사용자의 특정 상태 주문을 조회합니다.")
    public ApiResponse<?> getOrdersByStatus(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable OrderStatus status
    ) {
        User user = userService.findByProviderId(customUserDetails.getProviderId())
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND));

        List<OrderResponseDto> orders = user.getOrders().stream()
                .filter(order -> order.getStatus() == status)
                .map(OrderResponseDto::from)
                .collect(Collectors.toList());

        if (orders.isEmpty()) {
            return ApiResponse.onFailure(
                    ErrorCode.USER_ORDER_STATUS_NOT_FOUND,
                    status + " 상태의 주문이 없습니다."
            );
        }

        return ApiResponse.onSuccess(
                SuccessCode.USER_ORDERS_STATUS_SUCCESS,
                orders
        );
    }
}