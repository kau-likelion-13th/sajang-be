package likelion13th.shop.domain;

import jakarta.persistence.*;

@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
