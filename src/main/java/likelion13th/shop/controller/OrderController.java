package likelion13th.shop.controller;

import likelion13th.shop.DTO.request.OrderCreateRequest;
import likelion13th.shop.DTO.response.OrderResponseDto;
import likelion13th.shop.global.api.ApiResponse;
import likelion13th.shop.global.api.ErrorCode;
import likelion13th.shop.global.api.SuccessCode;
import likelion13th.shop.repository.OrderRepository;
import likelion13th.shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderRepository orderRepository;
    private final OrderService orderService;

    //1. 주문 생성
    /* 카카오 연결 후 사용
    @PostMapping
    public ResponseEntity<Order> createOrder(
            @AuthenticationPrincipal OAuth2User oAuth2User
            @RequestBody OrderCreateRequest request) {

        // ✅ 카카오 로그인한 유저의 고유 ID 가져오기
        String kakaoId = oAuth2User.getAttribute("id").toString();

        // ✅ 카카오 ID를 통해 유저 정보 조회
        User user = userRepository.findByKakaoId(kakaoId)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 사용자입니다."));

        request.setUserId(user.getId()); // ✅ 유저 ID를 Order 요청에 설정

        Order newOrder = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newOrder);
    }*/

    @PostMapping
    public ApiResponse<?> createOrder(@RequestBody OrderCreateRequest request) {
        OrderResponseDto newOrder = orderService.createOrder(request);
        if (newOrder == null) {
            return ApiResponse.onFailure(
                    ErrorCode.ORDER_CREATE_FAILED,
                    "주문 생성에 실패했습니다."
            );
        }
        return ApiResponse.onSuccess(
                SuccessCode.ORDER_CREATE_SUCCESS,
                newOrder
        );
    }

    //개별 주문 조회
    @GetMapping("/{orderId}")
    public ApiResponse<?> getOrderById(@PathVariable Long orderId) {
        OrderResponseDto order = orderService.getOrderById(orderId);
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

