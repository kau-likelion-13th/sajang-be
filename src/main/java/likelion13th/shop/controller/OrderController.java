package likelion13th.shop.controller;

import io.swagger.v3.oas.annotations.Operation;
import likelion13th.shop.DTO.request.OrderCreateRequest;
import likelion13th.shop.DTO.response.OrderResponseDto;
import likelion13th.shop.domain.Order;
import likelion13th.shop.domain.User;
import likelion13th.shop.global.api.ApiResponse;
import likelion13th.shop.global.api.ErrorCode;
import likelion13th.shop.global.api.SuccessCode;
import likelion13th.shop.login.auth.jwt.CustomUserDetails;
import likelion13th.shop.login.service.UserService;
import likelion13th.shop.repository.OrderRepository;
import likelion13th.shop.repository.UserRepository;
import likelion13th.shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final UserRepository userRepository;
    private final OrderService orderService;
    private final UserService userService;

    // 주문 생성
    @PostMapping
    @Operation(summary = "주문 생성", description = "로그인한 사용자의 주문을 생성합니다.")
    public ApiResponse<?> createOrder(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestBody OrderCreateRequest request) {

        // ✅ 1. 인증된 유저 정보 가져오기
        String providerId = customUserDetails.getProviderId();

        // ✅ 2. providerId 기반으로 유저 조회
        User user = userService.findByProviderId(providerId);

        // ✅ 3. 요청 객체에 User 정보 추가
        request.setUserId(user.getId());

        // ✅ 4. 주문 생성
        Optional<OrderResponseDto> newOrder = orderService.createOrder(request);

        // ✅ 5. 성공 응답 반환
        return ApiResponse.onSuccess(SuccessCode.ORDER_CREATE_SUCCESS, newOrder);
    }


    //개별 주문 조회
    @GetMapping("/{orderId}")
    public ApiResponse<?> getOrderById(@PathVariable Long orderId) {
        Optional<OrderResponseDto> order = orderService.getOrderById(orderId);
        if (order == null) {
            return ApiResponse.onFailure(
                    ErrorCode.ORDER_NOT_FOUND,
                    "해당 주문을 찾을 수 없습니다."
            );
        }
        return ApiResponse.onSuccess(
                SuccessCode.ORDER_GET_SUCCESS,
                order
        );
    }

    //모든 주문 목록 조회
    @GetMapping
    public ApiResponse<?> getAllOrders() {
        List<OrderResponseDto> orders = orderService.getAllOrders();
        if (orders.isEmpty()) {
            return ApiResponse.onFailure(
                    ErrorCode.ORDER_NOT_FOUND,
                    "등록된 주문이 없습니다."
            );
        }
        return ApiResponse.onSuccess(
                SuccessCode.ORDER_LIST_SUCCESS,
                orders
        );
    }

    //주문 취소
    @PutMapping("/{orderId}/cancel")
    public ApiResponse<?> cancelOrder(@PathVariable Long orderId) {
        boolean isCancelled = orderService.cancelOrder(orderId);
        if (!isCancelled) {
            return ApiResponse.onFailure(
                    ErrorCode.ORDER_CANCEL_FAILED,
                    "주문 취소에 실패했습니다."
            );
        }
        return ApiResponse.onSuccess(
                SuccessCode.ORDER_CANCEL_SUCCESS,
                "주문이 성공적으로 취소되었습니다."
        );
    }
}


