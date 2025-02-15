package likelion13th.shop.domain;

import jakarta.persistence.*;
import likelion13th.shop.domain.entity.BaseEntity;
import likelion13th.shop.login.auth.jwt.RefreshToken;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 카카오 고유 ID
    @Column(nullable = false, unique = true)
    private String providerId;

    // 카카오 닉네임, 중복 허용
    @Column(nullable = false)
    private String usernickname;

    // 휴대폰 번호 (선택 사항, 기본값 null)
    @Column(nullable = true)
    private String phoneNumber;

    // 계정 삭제 가능 여부 (기본 true)
    @Column(nullable = false)
    private boolean deleteable = true;

    // 마일리지 (기본값 0)
    @Column(nullable = false)
    private int mileage = 0;

    // 최근 총 구매액 (기본값 0)
    @Column(nullable = false)
    private int recentTotal = 0;

    // Refresh Token 관계 설정
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private RefreshToken auth;
}
