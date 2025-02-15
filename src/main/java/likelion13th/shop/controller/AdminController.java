package likelion13th.shop.controller;

import likelion13th.shop.domain.Category;
import likelion13th.shop.repository.CategoryRepository;
import likelion13th.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;

    //테스트 데이터 전체 삭제 API
    @DeleteMapping("/reset")
    public ResponseEntity<String> resetDatabase() {
        //중간테이블 삭제
        for (Category category : categoryRepository.findAll()) {
            category.getItems().clear(); // 카테고리와 아이템의 관계 해제
        }
        itemRepository.deleteAll();   // 상품 전체 삭제
        categoryRepository.deleteAll();  // 카테고리 전체 삭제
        return ResponseEntity.ok("테스트 데이터가 모두 삭제되었습니다.");
    }


}
