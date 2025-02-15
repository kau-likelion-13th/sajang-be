package likelion13th.shop.DTO;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderCreateRequest {
    private Long userId;  //일단은 프론트에서 `userId`를 받아 처리
    private Long itemId;
    private int quantity;
    private int mileageToUse;
}
