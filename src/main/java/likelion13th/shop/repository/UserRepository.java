package likelion13th.shop.repository;

import likelion13th.shop.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    // user_id 기반 사용자 찾기
    Optional<User> findById(Long userId);
    boolean existsById(Long userId);

    // providerId(카카오 고유 ID)로 사용자 찾기
    Optional<User> findByProviderId(String providerId);

    boolean existsByProviderId(String providerId);
}
