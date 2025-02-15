package likelion13th.shop.DTO;

import likelion13th.shop.domain.Category;
import likelion13th.shop.domain.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class ItemResponseDto {
    private Long id;
    private String name;
    private int price;
    private String brand;
    private String thumbnail;
    private List<String> categories;

    // Item → ItemResponseDto 변환
    public static ItemResponseDto from(Item item) {
        List<String> categoryNames = new ArrayList<>();
        for (Category category : item.getCategories()) {
            Category currentCategory = category; // 지역변수 사용
            categoryNames.add(currentCategory.getName());
        }

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
