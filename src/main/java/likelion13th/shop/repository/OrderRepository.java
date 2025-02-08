package likelion13th.shop.repository;

import likelion13th.shop.domain.Category;
import likelion13th.shop.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{

}
