package likelion13th.shop.controller;

import likelion13th.shop.DTO.CategoryCreateRequest;
import likelion13th.shop.DTO.ItemCreateRequest;
import likelion13th.shop.domain.Category;
import likelion13th.shop.domain.Item;
import likelion13th.shop.service.CategoryService;
import likelion13th.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {
    private final ItemService itemService;
    private final CategoryService categoryService;

    //카테고리 추가
    @PostMapping("/new")
    public  ResponseEntity<Category> createCategory(@RequestBody CategoryCreateRequest request){
        Category newCategory = categoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCategory);
    }

    //카테고리 전체 조회
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    //특정 카테고리에 속한 상품 목록 조회
    @GetMapping("/{categoryId}/items")
    public ResponseEntity<List<Item>> getItemsByCategory(@PathVariable Long categoryId) {
        List<Item> items = categoryService.getItemsByCategory(categoryId);
        return ResponseEntity.ok(items);
    }
}
