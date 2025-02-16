package likelion13th.shop.DTO.response;

import likelion13th.shop.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryResponseDto {
    private Long id;
    private String name;

    // Entity → DTO 변환
    public static CategoryResponseDto from(Category category) {
        return new CategoryResponseDto(
                category.getId(),
                category.getName()
        );
    }
}