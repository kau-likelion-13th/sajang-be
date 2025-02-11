package likelion13th.shop.service;

import jakarta.transaction.Transactional;
import likelion13th.shop.DTO.CategoryCreateRequest;
import likelion13th.shop.DTO.ItemCreateRequest;
import likelion13th.shop.domain.Category;
import likelion13th.shop.domain.Item;
import likelion13th.shop.repository.CategoryRepository;
import likelion13th.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;

    //카테고리 추가
    @Transactional
    public Category createCategory(CategoryCreateRequest request) {
        //DTO -> Entity
        Category category = new Category(
                request.getName()
        );

        return categoryRepository.save(category);
    }
    //모든 카테고리 조회
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    //상품 조회(카테고리별)
    public List<Item> getItemsByCategory(Long categoryId){
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return itemRepository.findByCategories(category);
    }
}
