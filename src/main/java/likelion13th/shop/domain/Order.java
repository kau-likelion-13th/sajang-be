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

    //생성자 -> 객체 생성될 때 자동으로 실행! 즉 초기 설정을 할 때 사용
    public Order(User user, Item item, int quantity) {
        this.user = user;
        this.item = item;
        this.quantity = quantity;
        this.created_date = LocalDateTime.now();
        this.status = OrderStatus.PROCESSING;
        this.totalPrice = item.getPrice() * quantity;
        this.finalPrice = getFinalPrice();
    }

    //Item, User 와 연관관계 설정
    @ManyToOne(fetch = FetchType.EAGER) //즉시 조회로 해봤어요
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;


}
