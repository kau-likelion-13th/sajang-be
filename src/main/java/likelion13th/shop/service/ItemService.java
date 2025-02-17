package likelion13th.shop.service;

import jakarta.transaction.Transactional;
import likelion13th.shop.DTO.response.ItemResponseDto;
import likelion13th.shop.domain.Item;
import likelion13th.shop.repository.CategoryRepository;
import likelion13th.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;

    //개별 상품 조회
    @Transactional
    public ItemResponseDto getItemById(Long itemId) {
        Item item = itemRepository.findById(itemId).orElse(null);
        return item != null ? ItemResponseDto.from(item) : null;
    }



}