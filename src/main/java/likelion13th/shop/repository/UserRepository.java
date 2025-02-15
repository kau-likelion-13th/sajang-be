package likelion13th.shop.repository;

import likelion13th.shop.domain.Category;
import likelion13th.shop.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    //save, findById, findAll은 자동생성
    List<User> findByUsernickname(String usernickname);

    //Optional<User> findByKakaoId(String kakaoId); // ✅ 카카오 ID로 유저 조회
}
