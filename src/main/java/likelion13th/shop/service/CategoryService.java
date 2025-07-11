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

    /** 카테고리 존재 여부 확인 **/
    public Category findCategoryById(Long categoryId){
        return categoryRepository.findById(categoryId)
                .orElseThrow(()-> new GeneralException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    /** 카테고리 별 상품 목록 조회 **/
    // DTO에 담아서 반환
    public List<ItemResponseDto> getItemsByCategory(Category category) {
        List<Item> items = category.getItems();
        return items.stream()
                .map(ItemResponseDto::from)
                .collect(Collectors.toList());
    }
}

