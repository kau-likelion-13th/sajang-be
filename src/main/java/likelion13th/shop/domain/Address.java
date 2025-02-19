package likelion13th.shop.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
//User 테이블 내부에 값타입으로 포함됨
@NoArgsConstructor
@AllArgsConstructor
//접근 제어자를 protected로 설정
@Getter
public class Address {
    // 나중에 지워야 함

    @Column(nullable = true)
    private String zipcode;

    @Lob
    @Column(nullable = true)
    //TEXT 타입을 java 엔티티에서 매핑
    private String address;

    @Lob
    @Column(nullable = true)
    private String address_detail;

}
