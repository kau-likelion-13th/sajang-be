package likelion13th.shop.login.auth.repository;

import likelion13th.shop.domain.User;
import likelion13th.shop.login.auth.jwt.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    // user 기반 RefreshToken 탐색 (토큰 발급 시 사용)
    Optional<RefreshToken> findByUser(User user);

    // JPQL로 강제 삭제
    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.user = :user")
    void deleteByUser(@Param("user") User user);


    // user_id(Long) 기반 RefreshToken 삭제 (로그아웃 및 유효성 검증 실패 시 사용)
    void deleteById(Long userId);
}
