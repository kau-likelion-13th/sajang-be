package likelion13th.shop.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Address {

    @Column(nullable = true)
    private String zipcode; // 우편번호

    @Lob
    @Column(nullable = true)
    private String address; // 기본 주소

    @Lob
    @Column(nullable = true)
    private String addressDetail; // 상세 주소
}