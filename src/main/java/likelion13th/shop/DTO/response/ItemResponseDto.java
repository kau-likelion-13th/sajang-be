package likelion13th.shop.DTO.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import likelion13th.shop.domain.Category;
import likelion13th.shop.domain.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ItemResponseDto {
    private Long id;
    private String name;
    private int price;
    private String brand;
    private String thumbnail;
    private List<String> categories;

    // Item → ItemResponseDto 변환
    public static ItemResponseDto from(Item item) {
        // 카테고리 이름 추출 (Stream API 사용)
        List<String> categoryNames = item.getCategories().stream() //스트림으로 변환
                .map(Category::getName) // Category 객체에서 이름 추출
                .collect(Collectors.toList()); // 다시 리스트로 모으기

        return new ItemResponseDto(
                item.getId(),
                item.getItem_name(),
                item.getPrice(),
                item.getBrand(),
                item.getThumbnail_img(),
                categoryNames
        );
    }
}
