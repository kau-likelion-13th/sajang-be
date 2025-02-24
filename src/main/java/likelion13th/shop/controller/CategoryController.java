package likelion13th.shop.controller;

import io.swagger.v3.oas.annotations.Operation;
import likelion13th.shop.DTO.response.ItemResponseDto;
import likelion13th.shop.domain.Category;
import likelion13th.shop.global.api.ApiResponse;
import likelion13th.shop.global.api.ErrorCode;
import likelion13th.shop.global.api.SuccessCode;
import likelion13th.shop.repository.CategoryRepository;
import likelion13th.shop.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;

    // 상품 조회(카테고리별)
    @GetMapping("/{categoryId}/items")
    @Operation(summary = "카테고리별 상품 조회", description = "상품을 카테고리 별로 조회합니다.")
    public ApiResponse<?> getItemsByCategory(@PathVariable Long categoryId) {
        Optional<Category> categoryOptional = categoryService.findCategoryById(categoryId);
        if (categoryOptional.isEmpty()) {
            return ApiResponse.onFailure(
                    ErrorCode.CATEGORY_NOT_FOUND,
                    "해당 카테고리를 찾을 수 없습니다."
            );
        }

        //카테고리 상품 조회
        List<ItemResponseDto> items = categoryService.getItemsByCategory(categoryOptional.get());
        //상품 없을 시 : 성공 응답 + 빈 리스트 반환
        if (items.isEmpty()) {
            return ApiResponse.onSuccess(
                    SuccessCode.CATEGORY_ITEMS_EMPTY,
                    Collections.emptyList()
            );
        }
        // 상품 있을 시 : 성공 응답
        return ApiResponse.onSuccess(SuccessCode.CATEGORY_ITEMS_GET_SUCCESS, items);
    }
}
