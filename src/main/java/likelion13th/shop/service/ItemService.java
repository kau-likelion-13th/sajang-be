package likelion13th.shop.service;

import jakarta.transaction.Transactional;
import likelion13th.shop.domain.Category;
import likelion13th.shop.domain.Item;
import likelion13th.shop.repository.CategoryRepository;
import likelion13th.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;

    //상품 추가
    @Transactional
    public Item saveItem(Item item) {
        return itemRepository.save(item);
    }

    //상품 조회(카테고리별)
    public List<Item> getItemsByCategory(Long categoryId){
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return itemRepository.findByCategory(category);
    }
    //상품 수정, 상품 삭제,



}
