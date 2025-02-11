package likelion13th.shop.service;

import jakarta.transaction.Transactional;
import likelion13th.shop.DTO.ItemCreateRequest;
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
    public Item createItem(ItemCreateRequest request) {
        //DTO -> Entity
        Item item = new Item(
                request.getItem_name(),
                request.getPrice(),
                request.getThumbnail_img(),
                request.getBrand()
        );

        // 카테고리 연관관계 추가
        if (request.getCategory_id() != null) {
            for (Long category_id : request.getCategory_id()) {
                Category category = categoryRepository.findById(category_id)
                        .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다: " + category_id));
                item.addCategory(category); //자동으로 category_item 테이블에 데이터 추가됨
            }
        }
        return itemRepository.save(item);
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
        Item item = itemRepository.findById(itemId)
                .orElseThrow(()-> new IllegalArgumentException("Item not found"));
        itemRepository.delete(item);
    }




}
