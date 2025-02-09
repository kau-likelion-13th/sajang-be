package likelion13th.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "likelion13th.shop.domain")  // 엔티티가 위치한 패키지 설정
@EnableJpaRepositories(basePackages = "likelion13th.shop.repository")  // JPA Repository 패키지 지정
public class ShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopApplication.class, args);
	}
}