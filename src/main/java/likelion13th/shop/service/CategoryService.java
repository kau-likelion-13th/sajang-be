package likelion13th.shop.service;

import likelion13th.shop.DTO.response.ItemResponseDto;
import likelion13th.shop.domain.Category;
import likelion13th.shop.domain.Item;
import likelion13th.shop.global.api.ErrorCode;
import likelion13th.shop.global.exception.GeneralException;
import likelion13th.shop.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    // 카테고리 조회 (Optional 반환)
    // 카테고리 별 상품 조회 시 카테고리 존재 여부
    // 여기서 Optional 을 처리해서 보내야함.
    /*public Optional<Category> findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId);
    }*/

    public Category findCategoryById(Long categoryId){
        // 레포지토리에서 찾고 empty 면 예외처리
        return categoryRepository.findById(categoryId)
                .orElseThrow(()-> new GeneralException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    // 상품 조회(카테고리별)
    public List<ItemResponseDto> getItemsByCategory(Category category) {
        List<Item> items = category.getItems();
        return items.stream()
                .map(ItemResponseDto::from)
                .collect(Collectors.toList());
    }
}

