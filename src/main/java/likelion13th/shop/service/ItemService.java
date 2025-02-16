package likelion13th.shop.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import likelion13th.shop.DTO.ItemCreateRequest;
import likelion13th.shop.DTO.ItemResponseDto;
import likelion13th.shop.DTO.ItemUpdateRequest;
import likelion13th.shop.domain.Category;
import likelion13th.shop.domain.Item;
import likelion13th.shop.global.api.ErrorCode;
import likelion13th.shop.repository.CategoryRepository;
import likelion13th.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;

    //상품 추가
    @Transactional
    public ItemResponseDto createItem(ItemCreateRequest request) {
        //DTO -> Entity
        Item item = new Item(
                request.getName(),
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
        Item savedItem = itemRepository.save(item);
        return ItemResponseDto.from(savedItem);
    }

    //개별 상품 조회
    @Transactional
    public ItemResponseDto getItemById(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("해당 상품을 찾을 수 없습니다."));

        return ItemResponseDto.from(item);
    }

    //모든 상품 조회
    @Transactional
    public List<ItemResponseDto> getAllItems() {
        List<Item> items = itemRepository.findAll();
        return items.stream()
                .map(ItemResponseDto::from)
                .collect(Collectors.toList());
    }

    //상품 수정 - 이름과 가격
    @Transactional
    //jPA에서는 얘 있으면 save()호출하지 않아도 자동으로 업데이트
    public ItemResponseDto updateItem(Long itemId, ItemUpdateRequest request) {
        //상품 조회 (존재하지 않으면 예외 발생)
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("해당 상품을 찾을 수 없습니다."));

        //필드 수정 (null 값이 아닌 경우만 업데이트)
        if (request.getName() != null) {
            item.setItem_name(request.getName());
        }
        if (request.getPrice() != null) {
            item.setPrice(request.getPrice());
        }

        return ItemResponseDto.from(item);
    }

    // 상품 삭제
    @Transactional
    public boolean deleteItem(Long itemId) {
        Optional<Item> itemOptional = itemRepository.findById(itemId);

        if (itemOptional.isEmpty()) {
            return false; // 상품 없음
        }

        try {
            Item item = itemOptional.get();
            // ✅ 중간 테이블 관계 제거
            item.getCategories().clear();
            itemRepository.save(item);

            // ✅ Item 삭제
            itemRepository.delete(item);
            return true; // 삭제 성공
        } catch (Exception e) {
            return false; // 삭제 실패
        }
    }
}