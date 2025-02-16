package likelion13th.shop.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import likelion13th.shop.DTO.response.ItemResponseDto;
import likelion13th.shop.domain.Item;
import likelion13th.shop.repository.CategoryRepository;
import likelion13th.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;

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

}