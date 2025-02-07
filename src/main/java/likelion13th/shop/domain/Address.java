package likelion13th.shop.domain;

import jakarta.persistence.*;

@Embeddable
//User 테이블 내부에 값타입으로 포함됨
public class Address {
    private Long id;

    private String zipcode;
    @Lob
    @Column(columnDefinition = "TEXT")
    //TEXT 타입을 java 엔티티에서 매핑
    private String address;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String address_detail;



}
