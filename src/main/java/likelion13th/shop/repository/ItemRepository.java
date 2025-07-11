package likelion13th.shop.repository;


import likelion13th.shop.domain.Category;
import likelion13th.shop.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    //save, findById, findAll 같은 기본 기능 자동으로 제공됨
}
