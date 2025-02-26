package likelion13th.shop.login.auth.jwt;

import likelion13th.shop.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * ✅ Spring Security 사용자 인증 정보 (필수 정보만 포함)
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CustomUserDetails implements UserDetails {
    // user_id 기반 인증 필드
    private Long userId;          // 유저 ID (PK)
    private String providerId;
    private String usernickname;  // 카카오 닉네임
    private Collection<? extends GrantedAuthority> authorities;


    public CustomUserDetails(User user) {
        this.userId = user.getId();
        this.providerId = user.getProviderId();
        this.usernickname = user.getUsernickname();
    }

    // ✅ CustomUserDetails 생성자 추가
    public CustomUserDetails(String providerId, String password, Collection<? extends GrantedAuthority> authorities) {
        this.providerId = providerId;
        this.userId = null;  // OAuth2 로그인 시 UserId는 나중에 조회
        this.usernickname = null;
        this.authorities = authorities;
    }

    /**
     * ✅ User 엔티티 → CustomUserDetails 변환
     */
    public static CustomUserDetails fromEntity(User entity) {
        return CustomUserDetails.builder()
                .userId(entity.getId())                 // user_id (PK)
                .providerId(entity.getProviderId())     // provider_id
                .usernickname(entity.getUsernickname()) // 카카오 닉네임
                .build();
    }

    /**
     * ✅ CustomUserDetails → User 엔티티 변환
     */
    public User toEntity() {
        return User.builder()
                .id(this.userId)                        // user_id
                .providerId(this.providerId)            // provider_id
                .usernickname(this.usernickname)       // 카카오 닉네임
                .build();
    }

    /**
     * ✅ Spring Security 필수 메서드 구현부
     * 소셜 로그인 기반으로 비밀번호는 사용하지 않음
     */
    @Override
    public String getUsername() {
        return this.providerId; // user_id 반환 (String 타입)
    }

    /**
     * ✅ 권한 부여 (기본 USER 권한)
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return ""; // 소셜 로그인은 비밀번호 없음
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정 만료 여부 (항상 true)
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정 잠금 여부 (항상 true)
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 자격 증명(비밀번호) 만료 여부 (항상 true)
    }

    @Override
    public boolean isEnabled() {
        return true; // 계정 활성화 여부 (항상 true)
    }


}
