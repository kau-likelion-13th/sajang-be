package likelion13th.shop.domain;

import jakarta.persistence.*;
import likelion13th.shop.domain.entity.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orders") //예약어 회피
@Getter
@Setter
@NoArgsConstructor
//파라미터가 없는 디폴트 생성자 자동으로 생성
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="order_id", nullable = false, unique = true)
    private Long id;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private int totalPrice; //기존 주문 내역을 유지하기 위해

    @Column(nullable = false)
    private int finalPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    //Item, User 와 연관관계 설정
    @ManyToOne(fetch = FetchType.EAGER) //즉시 조회로 해봤어요
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    //생성자 -> 객체 생성될 때 자동으로 실행! 즉 초기 설정을 할 때 사용
    public Order(User user, Item item, int quantity, int mileageToUse) {
        this.user = user;
        this.item = item;
        this.quantity = quantity;
        this.status = OrderStatus.PROCESSING;
        this.totalPrice = item.getPrice() * quantity;
        this.finalPrice = calculateFinalPrice(mileageToUse);
    }
    //마일리지 적용 로직
    private int calculateFinalPrice(int mileageToUse) {
        int finalPrice = totalPrice - mileageToUse;
        return Math.max(finalPrice, 0);  // 최소 결제 금액 0원 보장
    }

    //양방향 편의 메서드
    public void setUser(User user) {
        this.user=user;
        user.getOrders().add(this); // 반대쪽 객체에도 연관관계를 설정
    }

    public void setItem(Item item) {
        this.item = item;
        if (!item.getOrders().contains(this)) {
            item.getOrders().add(this);
        }
    }
}
