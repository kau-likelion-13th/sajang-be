package likelion13th.shop.service;

import jakarta.transaction.Transactional;
import likelion13th.shop.DTO.ItemUpdateRequest;
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
        return itemRepository.findByCategories(category);
    }

    //개별 상품 조회
    public Item getItemById(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() -> new RuntimeException("Item not found"));
    }

    //모든 상품 조회
    public List<Item> getAllItems(){
        return itemRepository.findAll();
    }

    //상품 수정 - 이름과 가격
    public Item updateItem(Long itemId, ItemUpdateRequest request){
        //상품 조회 (존재하지 않으면 예외 발생)
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품을 찾을 수 없습니다."));

        //필드 수정 (null 값이 아닌 경우만 업데이트)
        if (request.getName() != null) {
            item.setItem_name(request.getName());
        }
        if (request.getPrice() != null && request.getPrice() > 0) {
            item.setPrice(request.getPrice());
        }
        return item;
    }

    // 상품 삭제
    public void deleteItem(Long itemId){
        itemRepository.deleteById(itemId);
    }




}
