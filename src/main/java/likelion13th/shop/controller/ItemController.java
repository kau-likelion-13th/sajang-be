package likelion13th.shop.controller;

import likelion13th.shop.DTO.ItemCreateRequest;
import likelion13th.shop.DTO.ItemUpdateRequest;
import likelion13th.shop.domain.Item;
import likelion13th.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    //상품 추가
    @PostMapping("/new")
    public  ResponseEntity<Item> createItem(@RequestBody ItemCreateRequest request){
        Item newItem = itemService.createItem(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newItem);
    }

    //상품 조회
    //개별 상품 조회
    @GetMapping("/{itemId}")
    public ResponseEntity<Item> getItemById(@PathVariable Long itemId) {
        Item item = itemService.getItemById(itemId);
        return ResponseEntity.ok(item);
    }

}
