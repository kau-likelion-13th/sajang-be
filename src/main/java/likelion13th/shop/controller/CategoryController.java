package likelion13th.shop.controller;

import likelion13th.shop.DTO.request.CategoryCreateRequest;
import likelion13th.shop.DTO.response.CategoryResponseDto;
import likelion13th.shop.DTO.response.ItemResponseDto;
import likelion13th.shop.global.api.ApiResponse;
import likelion13th.shop.global.api.ErrorCode;
import likelion13th.shop.global.api.SuccessCode;
import likelion13th.shop.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    //카테고리 추가
    @PostMapping("/new")
    public ApiResponse<CategoryResponseDto> createCategory(@RequestBody CategoryCreateRequest request){
        CategoryResponseDto newCategory = categoryService.createCategory(request);
        return  ApiResponse.onSuccess(SuccessCode.CATEGORY_CREATE_SUCCESS, newCategory);
    }

    //카테고리 전체 조회
    @GetMapping
    public ApiResponse<?> getAllCategories() {
        List<CategoryResponseDto> categories = categoryService.getAllCategories();
        if (categories.isEmpty()) {
            return ApiResponse.onFailure(ErrorCode.CATEGORY_NOT_FOUND, "조회된 카테고리가 없습니다.");
        }
        return ApiResponse.onSuccess(SuccessCode.CATEGORY_GET_SUCCESS, categories);
    }

    //특정 카테고리에 속한 상품 목록 조회
    @GetMapping("/{categoryId}/items")
    public ApiResponse<?> getItemsByCategory(@PathVariable Long categoryId) {
        List<ItemResponseDto> items = categoryService.getItemsByCategory(categoryId);
        if (items.isEmpty()) {
            return ApiResponse.onFailure(ErrorCode.CATEGORY_NOT_FOUND,  "해당 카테고리에 등록된 상품이 없습니다.");
        }
        return ApiResponse.onSuccess(SuccessCode.CATEGORY_ITEMS_GET_SUCCESS, items);
    }
}
