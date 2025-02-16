package likelion13th.shop.service;

import jakarta.transaction.Transactional;
import likelion13th.shop.DTO.request.CategoryCreateRequest;
import likelion13th.shop.DTO.response.CategoryResponseDto;
import likelion13th.shop.DTO.response.ItemResponseDto;
import likelion13th.shop.domain.Category;
import likelion13th.shop.domain.Item;
import likelion13th.shop.repository.CategoryRepository;
import likelion13th.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    //카테고리 추가
    @Transactional
    public CategoryResponseDto createCategory(CategoryCreateRequest request) {
        //DTO -> Entity
        Category category = new Category(request.getName());
        Category savedCategory = categoryRepository.save(category);
        return CategoryResponseDto.from(savedCategory);
    }

    //모든 카테고리 조회
    public List<CategoryResponseDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(CategoryResponseDto::from)
                .collect(Collectors.toList());
    }

    //상품 조회(카테고리별)
    public List<ItemResponseDto> getItemsByCategory(Long categoryId){
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        List<Item> items = category.getItems();
        return items.stream()
                .map(ItemResponseDto::from)
                .collect(Collectors.toList());
    }
}

