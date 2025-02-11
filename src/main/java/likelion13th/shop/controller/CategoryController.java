package likelion13th.shop.controller;

import likelion13th.shop.domain.Category;
import likelion13th.shop.domain.Item;
import likelion13th.shop.service.CategoryService;
import likelion13th.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {
    private final ItemService itemService;
    private final CategoryService categoryService;

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
