package likelion13th.shop.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ItemUpdateRequest {
    private String name;
    private Integer price;
}
