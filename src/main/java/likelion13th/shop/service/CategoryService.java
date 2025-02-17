package likelion13th.shop.service;

import likelion13th.shop.DTO.response.ItemResponseDto;
import likelion13th.shop.domain.Category;
import likelion13th.shop.domain.Item;
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
    public Optional<Category> findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId);
    }

    // 상품 조회(카테고리별)
    public List<ItemResponseDto> getItemsByCategory(Category category){
        List<Item> items = category.getItems();
        return items.stream()
                .map(ItemResponseDto::from)
                .collect(Collectors.toList());
    }
}

