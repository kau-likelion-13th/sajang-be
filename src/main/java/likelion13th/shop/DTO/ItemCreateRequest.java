package likelion13th.shop.DTO;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ItemCreateRequest {
    private String id;
    private String name;
    private int price;
    private String thumbnail_img;
    private String brand;

    //카테고리
    public List<Long> category_id;
}
