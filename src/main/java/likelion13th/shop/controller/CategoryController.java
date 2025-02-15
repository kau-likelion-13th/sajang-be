package likelion13th.shop.controller;

import likelion13th.shop.DTO.CategoryCreateRequest;
import likelion13th.shop.DTO.CategoryResponseDto;
import likelion13th.shop.DTO.ItemResponseDto;
import likelion13th.shop.service.CategoryService;
import likelion13th.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    //카테고리 추가
    @PostMapping("/new")
    public  ResponseEntity<CategoryResponseDto> createCategory(@RequestBody CategoryCreateRequest request){
        CategoryResponseDto newCategory = categoryService.createCategory(request);
        URI location = URI.create("/category/" + newCategory.getId());
        return ResponseEntity.created(location).body(newCategory);
    }

    //카테고리 전체 조회
    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
        List<CategoryResponseDto> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    //특정 카테고리에 속한 상품 목록 조회
    @GetMapping("/{categoryId}/items")
    public ResponseEntity<List<ItemResponseDto>> getItemsByCategory(@PathVariable Long categoryId) {
        List<ItemResponseDto> items = categoryService.getItemsByCategory(categoryId);
        return ResponseEntity.ok(items);
    }
}
