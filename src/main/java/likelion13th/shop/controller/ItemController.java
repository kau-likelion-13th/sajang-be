package likelion13th.shop.controller;

import likelion13th.shop.DTO.ItemCreateRequest;
import likelion13th.shop.DTO.ItemResponseDto;
import likelion13th.shop.DTO.ItemUpdateRequest;
import likelion13th.shop.global.api.ApiResponse;
import likelion13th.shop.global.api.ErrorCode;
import likelion13th.shop.global.api.SuccessCode;
import likelion13th.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    //상품 추가
    //테스트를 위해 남겨둠?
    @PostMapping("/new")
    public ApiResponse<?> createItem(@RequestBody ItemCreateRequest request){
        ItemResponseDto newItem = itemService.createItem(request);
        if (newItem == null) {
            return ApiResponse.onFailure(
                    ErrorCode.ITEM_CREATE_FAILED,
                    "상품 등록 중 문제가 발생했습니다."
            );
        }
        return ApiResponse.onSuccess(
                SuccessCode.ITEM_CREATE_SUCCESS,
                newItem
        );
    }

    //상품 수정
    @PatchMapping("/{itemId}")
    public ApiResponse<?> updateItem(
            @PathVariable Long itemId,
            @RequestBody ItemUpdateRequest request){
        ItemResponseDto updatedItem = itemService.updateItem(itemId, request);
        if (updatedItem == null) {
            return ApiResponse.onFailure(
                    ErrorCode.ITEM_NOT_FOUND,
                    "수정할 상품을 찾을 수 없습니다."
            );
        }
        return ApiResponse.onSuccess(
                SuccessCode.ITEM_UPDATE_SUCCESS,
                updatedItem
        );
    }

    //상품 삭제
    @DeleteMapping("/{itemId}")
    public ApiResponse<?> deleteItem(@PathVariable Long itemId) {
        boolean isDeleted = itemService.deleteItem(itemId);
        if (!isDeleted) {
            return ApiResponse.onFailure(
                    ErrorCode.ITEM_DELETE_FAILED,
                    "상품 삭제에 실패했습니다."
            );
        }
        return ApiResponse.onSuccess(
                SuccessCode.ITEM_DELETE_SUCCESS,
                "상품이 성공적으로 삭제되었습니다."
        );
    }

    //상품 조회
    //개별 상품 조회
    @GetMapping("/{itemId}")
    public ApiResponse<?> getItemById(@PathVariable Long itemId) {
        ItemResponseDto item = itemService.getItemById(itemId);
        if (item == null) {
            return ApiResponse.onFailure(
                    ErrorCode.ITEM_NOT_FOUND,
                    "해당 상품을 찾을 수 없습니다."
            );
        }
        return ApiResponse.onSuccess(
                SuccessCode.ITEM_GET_SUCCESS,
                item
        );
    }

}
