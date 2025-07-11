package likelion13th.shop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import likelion13th.shop.DTO.response.ItemResponseDto;
import likelion13th.shop.domain.Category;
import likelion13th.shop.global.api.ApiResponse;
import likelion13th.shop.global.api.ErrorCode;
import likelion13th.shop.global.api.SuccessCode;
import likelion13th.shop.global.exception.GeneralException;
import likelion13th.shop.repository.CategoryRepository;
import likelion13th.shop.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Tag(name = "카테고리", description = "카테고리 관련 API 입니다.")
@Slf4j
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    // 상품 조회(카테고리별)
    // 컨트롤러에서 Optional 처리하고 있음
    // 컨트롤러에서는 예외처리만 하고자 함!
    /** 카테고리 별 상품 조회**/
    @GetMapping("/{categoryId}/items")
    @Operation(summary = "카테고리별 상품 조회", description = "상품을 카테고리 별로 조회합니다.")
    public ApiResponse<?> getItemsByCategory(@PathVariable Long categoryId) {
        log.info("[STEP 1] 카테고리 상품 조회 요청... categoryId={}", categoryId);

        // 이제 Exception 잡기!
        try {
            // 카테고리 존재 여부 확인
            Category category = categoryService.findCategoryById(categoryId);

            List<ItemResponseDto> items = categoryService.getItemsByCategory(category);

            //상품 없을 시 : 성공 응답 + 빈 리스트 반환
            if (items.isEmpty()) {
                log.info("[STEP 2] 카테고리에 상품 없음");
                return ApiResponse.onSuccess(SuccessCode.CATEGORY_ITEMS_EMPTY, Collections.emptyList());
            }

            log.info("[STEP 2] 카테고리 상품 조회 성공");
            return ApiResponse.onSuccess(SuccessCode.CATEGORY_ITEMS_GET_SUCCESS, items);
        } catch (GeneralException e) {
            log.error("❌ [ERROR] 카테고리 조회 중 예외 발생: {}", e.getReason().getMessage());
            throw e; // 다시 던져서 전역 예외 처리기가 처리하게 함
        } catch (Exception e){
            log.error("❌ [ERROR] 알 수 없는 예외 발생: {}", e.getMessage());
            throw new GeneralException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
