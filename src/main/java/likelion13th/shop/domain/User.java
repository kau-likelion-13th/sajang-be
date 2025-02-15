package likelion13th.shop.domain;

import jakarta.persistence.*;
import likelion13th.shop.domain.entity.BaseEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
//파라미터가 없는 디폴트 생성자 자동으로 생성
@AllArgsConstructor
//클래스의 모든 필드 값을 파라미터로 받는 생성자 자동으로 생성
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    @Setter(AccessLevel.PRIVATE)
    private Long id;

    @Column(nullable = false, unique = true)
    private String usernickname;

    @Column(nullable = false, unique = true)
    private String providerId;

    @Column(nullable = false)
    @Setter
    private Boolean deletable;

    @Column(nullable = false)
    private String phoneNumber;
    //Long -> String : Long은 0이 사라지는구나

    @Column(nullable = false)
    @Setter(AccessLevel.NONE) //비즈니스 메서드 만으로 관리
    private int mileage=0;

    @Column(nullable = false)
    @Setter(AccessLevel.NONE)
    private int recent_total=0;

    //erd에는 있길래
     //private String grade;
    //private String profile_img_path;
    //private String password;

    @Embedded
    private Address address;

    //Order과 일대다 연관관계 설정
    @OneToMany(mappedBy="user", cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<Order>();

    public void addOrder(Order order) {
        this.orders.add(order);
        order.setUser(this);
    }

    //마일리지 차감 로직
    public void useMileage(int mileage){
        if (mileage <= 0) {
            throw new IllegalArgumentException("사용할 마일리지는 0보다 커야 합니다.");
        }
        if (this.mileage < mileage) {
            throw new IllegalArgumentException("마일리지가 부족합니다.");
        }

        this.mileage -= mileage;
    }
    //마일리지 적립 로직
    public void addMileage(int mileage) {
        if (mileage <= 0) {
            throw new IllegalArgumentException("적립할 마일리지는 0보다 커야 합니다.");
        }

        this.mileage += mileage;
    }

    // 결제 금액 업데이트
    public void updateRecentTotal(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("최근 결제 금액은 0보다 커야 합니다.");
        }
        this.recent_total += amount;
    }
}
