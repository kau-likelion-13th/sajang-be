package likelion13th.shop.controller;

import likelion13th.shop.DTO.request.OrderCreateRequest;
import likelion13th.shop.DTO.response.OrderResponseDto;
import likelion13th.shop.domain.Order;
import likelion13th.shop.domain.User;
import likelion13th.shop.global.api.ApiResponse;
import likelion13th.shop.global.api.ErrorCode;
import likelion13th.shop.global.api.SuccessCode;
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

    // 주문 생성
    @PostMapping
    public ApiResponse<?> createOrder(
            @AuthenticationPrincipal OAuth2User oAuth2User,
            @RequestBody OrderCreateRequest request) {

        // 카카오 로그인한 유저의 providerId (카카오 고유 ID) 가져오기
        if (oAuth2User == null || oAuth2User.getAttribute("id") == null) {
            return ApiResponse.onFailure(
                    ErrorCode.USER_NOT_AUTHENTICATED,
                    "카카오 로그인 정보가 없습니다."
            );
        }

        String providerId = oAuth2User.getAttribute("id").toString();
        //kakaoId -> providerId 함

        // 카카오 ID를 통해 유저 정보 조회
        Optional<User> userOptional = userRepository.findByProviderId(providerId);
        if (userOptional.isEmpty()) {
            return ApiResponse.onFailure(
                    ErrorCode.USER_NOT_FOUND,
                    "등록되지 않은 사용자입니다."
            );
        }

        // 요청에 유저 ID 추가
        User user = userOptional.get();

        // 주문 생성 시도
        Optional<OrderResponseDto> newOrder = orderService.createOrder(request, user.getId());

        // 실패 상황에 따른 통일된 응답 처리
        if (newOrder.isEmpty()) {
            return ApiResponse.onFailure(
                    ErrorCode.ORDER_CREATE_FAILED,
                    "주문 생성에 실패했습니다. (사용자, 상품, 마일리지 문제일 수 있습니다.)"
            );
        }

        // 성공 시 `onSuccess` 반환
        return ApiResponse.onSuccess(
                SuccessCode.ORDER_CREATE_SUCCESS,
                newOrder.get()
        );
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


