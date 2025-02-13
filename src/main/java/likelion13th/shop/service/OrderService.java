package likelion13th.shop.service;

import jakarta.transaction.Transactional;
import likelion13th.shop.DTO.OrderCreateRequest;
import likelion13th.shop.domain.Item;
import likelion13th.shop.domain.Order;
import likelion13th.shop.domain.OrderStatus;
import likelion13th.shop.domain.User;
import likelion13th.shop.repository.ItemRepository;
import likelion13th.shop.repository.OrderRepository;
import likelion13th.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public Order createOrder(OrderCreateRequest request) {
        //이미 `userId`가 컨트롤러에서 설정됨 → 여기서 별도로 조회할 필요 없음
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Item item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        //총 주문 금액 계산
        int totalPrice = item.getPrice() * request.getQuantity();
        //마일리지 적용
        int mileageToUse = request.getMileageToUse();
        if (mileageToUse > user.getMileage()) {
            throw new IllegalArgumentException("보유한 마일리지를 초과하여 사용할 수 없습니다.");
        }

        int finalPrice = totalPrice - mileageToUse; // 최종 결제 금액
        if (finalPrice < 0) {
            finalPrice = 0; // 마일리지가 초과 사용되지 않도록 방지
        }
        user.useMileage(mileageToUse);
        //주문 생성과 동시에 배송 중으로 설정
        //주문 생성 및 저장
        Order order = new Order(user, item, request.getQuantity(), finalPrice);
        order.setStatus(OrderStatus.PROCESSING);
        return orderRepository.save(order);
    }
}
