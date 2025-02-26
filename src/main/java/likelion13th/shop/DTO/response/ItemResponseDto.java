package likelion13th.shop.DTO.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import likelion13th.shop.domain.Category;
import likelion13th.shop.domain.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private boolean isNew;

    @JsonProperty("isNew")
    public boolean getIsNew() {
        return isNew;
    }

    // Item → ItemResponseDto 변환
    public static ItemResponseDto from(Item item) {
        return new ItemResponseDto(
                item.getId(),
                item.getItem_name(),
                item.getPrice(),
                item.getBrand(),
                item.getThumbnail_img(),
                item.isNew()
        );
    }
}
