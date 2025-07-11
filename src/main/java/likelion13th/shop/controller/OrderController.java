package likelion13th.shop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import likelion13th.shop.DTO.request.OrderCreateRequest;
import likelion13th.shop.DTO.response.OrderResponse;
import likelion13th.shop.domain.User;
import likelion13th.shop.global.api.ApiResponse;
import likelion13th.shop.global.api.ErrorCode;
import likelion13th.shop.global.api.SuccessCode;
import likelion13th.shop.global.exception.GeneralException;
import likelion13th.shop.login.auth.jwt.CustomUserDetails;
import likelion13th.shop.login.service.UserService;
import likelion13th.shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Tag(name = "주문", description = "주문 관련 API 입니다.")
@Slf4j
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;

    /** 주문 생성 **/
    @PostMapping
    @Operation(summary = "주문 생성", description = "로그인한 사용자의 주문을 생성합니다.")
    public ApiResponse<?> createOrder(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody OrderCreateRequest request
    ) {
        User user = userService.getAuthenticatedUser(customUserDetails.getProviderId());
        log.info("[STEP 1] 주문 생성 요청 수신...");
        OrderResponse newOrder = orderService.createOrder(request, user);
        log.info("[STEP 2] 주문 생성 성공");
        return ApiResponse.onSuccess(SuccessCode.ORDER_CREATE_SUCCESS, newOrder);
    }


    /** 개별 주문 조회 **/
    @GetMapping("/{orderId}")
    @Operation(summary = "주문 개별 조회", description = "로그인한 사용자의 주문을 개별 조회합니다.")
    public ApiResponse<?> getOrderById(@PathVariable Long orderId) {
        log.info("[STEP 1] 개별 주문 조회 요청 수신...");
        OrderResponse order = orderService.getOrderById(orderId);
        log.info("[STEP 2] 개별 주문 조회 성공");
        return ApiResponse.onSuccess(SuccessCode.ORDER_GET_SUCCESS, order);

    }

    /** 모든 주문 목록 조회 **/
    @GetMapping
    @Operation(summary = "모든 주문 조회", description = "로그인한 사용자의 모든 주문을 목록으로 조회합니다.")
    public ApiResponse<?> getAllOrders(
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        User user = userService.getAuthenticatedUser(customUserDetails.getProviderId());

        List<OrderResponse> orders = orderService.getAllOrders(user);

        // 주문이 없더라도 성공 응답 + 빈 리스트 반환
        if (orders.isEmpty()) {
            return ApiResponse.onSuccess(SuccessCode.ORDER_LIST_EMPTY, Collections.emptyList());
        }
        return ApiResponse.onSuccess(SuccessCode.ORDER_LIST_SUCCESS, orders);
    }

    /** 주문 취소 **/
    @PutMapping("/{orderId}/cancel")
    @Operation(summary = "주문 취소", description = "로그인한 사용자의 주문을 취소합니다.")
    public ApiResponse<?> cancelOrder(@PathVariable Long orderId) {
        log.info("[STEP 1] 주문 취소 요청 수신");
        orderService.cancelOrder(orderId);
        log.info("[STEP 2] 주문 취소 성공");
        return ApiResponse.onSuccess(SuccessCode.ORDER_CANCEL_SUCCESS, "주문이 성공적으로 취소되었습니다.");

    }
}


