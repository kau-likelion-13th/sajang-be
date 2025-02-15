package likelion13th.shop.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
//User 테이블 내부에 값타입으로 포함됨
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
//접근 제어자를 protected로 설정
@Getter
public class Address {

    @Column(nullable = false)
    private String zipcode;

    @Lob
    @Column(nullable = false)
    //TEXT 타입을 java 엔티티에서 매핑
    private String address;

    @Lob
    @Column(nullable = false)
    private String address_detail;

}
