package likelion13th.shop.login.auth.repository;

import jakarta.transaction.Transactional;
import likelion13th.shop.domain.User;
import likelion13th.shop.login.auth.jwt.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository  // ✅ 추가 필요!
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    // (reissue 시 사용)
    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    // user 기반 RefreshToken 탐색 (토큰 발급 시 사용)
    Optional<RefreshToken> findByUser(User user);

    // ✅ user 기반 삭제
    @Transactional
    void deleteByUser(User user);

    // user_id(Long) 기반 RefreshToken 삭제 (로그아웃 및 유효성 검증 실패 시 사용)
    void deleteById(Long userId);
}
