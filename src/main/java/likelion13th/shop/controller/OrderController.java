package likelion13th.shop.controller;

import likelion13th.shop.DTO.OrderCreateRequest;
import likelion13th.shop.domain.Order;
import likelion13th.shop.repository.OrderRepository;
import likelion13th.shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Order> createOrder(@RequestBody OrderCreateRequest request) {
        Order newOrder = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newOrder);
    }

    //2. 특정 주문 조회
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long orderId) {
        Order order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }


    //3. 모든 주문 목록 조회
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    //4. 주문 취소
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Order> cancelOrder(@PathVariable Long orderId) {
        Order canceledOrder = orderService.cancelOrder(orderId);
        return ResponseEntity.ok(canceledOrder);
    }

}

