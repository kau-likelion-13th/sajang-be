package likelion13th.shop.controller;

import io.swagger.v3.oas.annotations.Operation;
import likelion13th.shop.DTO.response.ItemResponseDto;
import likelion13th.shop.global.api.ApiResponse;
import likelion13th.shop.global.api.ErrorCode;
import likelion13th.shop.global.api.SuccessCode;
import likelion13th.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    //개별 상품 조회
    /*@GetMapping("/{itemId}")
    @Operation(summary = "상품 개별 조회", description = "상품을 개별 조회합니다.")
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
    }*/

}
