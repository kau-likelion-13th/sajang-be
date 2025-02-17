package likelion13th.shop.DTO.response;


import likelion13th.shop.domain.Order;
import likelion13th.shop.global.constant.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderResponseDto {
    private Long orderId;
    private String usernickname;
    private String item_name;
    private int quantity;
    private int totalPrice;
    private int finalPrice;
    private int UseMileage; //남은 마일리지
    private OrderStatus status;

    public static OrderResponseDto from(Order order) {
        return new OrderResponseDto(
                order.getId(),
                order.getUser().getUsernickname(),
                order.getItem().getItem_name(),
                order.getQuantity(),
                order.getTotalPrice(),
                order.getFinalPrice(),
                order.getUser().getMileage(),
                order.getStatus()

        );
    }
}
