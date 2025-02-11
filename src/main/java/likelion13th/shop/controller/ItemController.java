package likelion13th.shop.controller;

import likelion13th.shop.DTO.ItemUpdateRequest;
import likelion13th.shop.domain.Item;
import likelion13th.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    //상품 추가

    //상품 조회

    //상품 수정
    @PutMapping("/{itemId}")
    public ResponseEntity<Item> updateItem(
            @PathVariable Long itemId,
            @RequestBody ItemUpdateRequest request){

        Item updatedItem = itemService.updateItem(itemId, request);
        return ResponseEntity.ok(updatedItem);
    }
    //상품 삭제
}
