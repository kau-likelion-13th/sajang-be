package likelion13th.shop.DTO.response;

import likelion13th.shop.domain.Order;
import likelion13th.shop.domain.User;
import likelion13th.shop.global.constant.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class UserInfoResponse {
    private String usernickname;
    private int recentTotal;
    private int maxMileage;
    private List<UserOrderSummary> orderSummaries;

    public static UserInfoResponse from(User user) {
        List<UserOrderSummary> orderSummaries = user.getOrders().stream()
                .map(UserOrderSummary::from)
                .collect(Collectors.toList());

        return new UserInfoResponse(
                user.getUsernickname(),
                user.getRecentTotal(),
                user.getMaxMileage(),
                orderSummaries
        );
    }

    @Getter
    @AllArgsConstructor
    public static class UserOrderSummary {
        private Long orderId;
        private String itemName;
        private int quantity;
        private int finalPrice;
        private OrderStatus status;
        private String statusDescription;

        public static UserOrderSummary from(Order order) {
            return new UserOrderSummary(
                    order.getId(),
                    order.getItem().getItem_name(),
                    order.getQuantity(),
                    order.getFinalPrice(),
                    order.getStatus(),
                    getOrderStatusDescription(order.getStatus())
            );
        }
    }

    // OrderStatus 해석을 위한 유틸리티 메서드
    private static String getOrderStatusDescription(OrderStatus status) {
        switch (status) {
            case PROCESSING:
                return "배송 중";
            case COMPLETE:
                return "배송 완료";
            case CANCEL:
                return "주문 취소";
            default:
                return "알 수 없음";
        }
    }
}