package likelion13th.shop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue
    @Column(name="order_id")
    private Long id;

    private int quantity;
    private int totalPrice;
    private int finalPrice;
    private LocalDateTime created_date;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    

}
