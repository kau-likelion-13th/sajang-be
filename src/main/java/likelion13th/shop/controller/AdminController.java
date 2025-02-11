package likelion13th.shop.controller;

import likelion13th.shop.DTO.ItemCreateRequest;
import likelion13th.shop.DTO.ItemUpdateRequest;
import likelion13th.shop.domain.Category;
import likelion13th.shop.domain.Item;
import likelion13th.shop.repository.CategoryRepository;
import likelion13th.shop.repository.ItemRepository;
import likelion13th.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final ItemService itemService;

    //상품 추가
    @PostMapping("/new")
    public  ResponseEntity<Item> createItem(@RequestBody ItemCreateRequest request){
        Item newItem = itemService.createItem(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newItem);
    }

    //상품 수정
    @PutMapping("/{itemId}")
    public ResponseEntity<Item> updateItem(
            @PathVariable Long itemId,
            @RequestBody ItemUpdateRequest request){

        Item updatedItem = itemService.updateItem(itemId, request);
        return ResponseEntity.ok(updatedItem);
    }
    //상품 삭제
    @DeleteMapping("/{itemId}")
    public ResponseEntity<String> deleteItem(@PathVariable Long itemId) {
        itemService.deleteItem(itemId);
        return ResponseEntity.ok("상품이 삭제되었습니다.");
    }
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
