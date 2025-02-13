package likelion13th.shop.domain;

import jakarta.persistence.*;
import likelion13th.shop.domain.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @Column(name="user_id", nullable = false, unique = true)
    private Long id;

    @Column(nullable = false, unique = true)
    private String usernickname;

    @Column(nullable = false, unique = true)
    private String providerId;

    @Column(nullable = false)
    private Boolean deleteable;

    @Column(nullable = false)
    private Long phoneNumer;

    @Column(nullable = false)
    private int mileage=0;

    @Column(nullable = false)
    private int recent_total=0;

    //erd에는 있길래
     //private String grade;
    //private String profile_img_path;
    //private String password;

    @Embedded
    private Address address;

    //Order과 일대다 연관관계 설정
    @OneToMany(mappedBy="user")
    private List<Order> orders = new ArrayList<Order>();

    //마일리지 차감 로직
    public void useMileage(int mileage){
        this.mileage -= mileage;
    }
    //마일리지 적립 로직
    public void addMileage(int mileage) {
        this.mileage += mileage;
    }
}
