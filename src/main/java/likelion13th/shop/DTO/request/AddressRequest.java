package likelion13th.shop.DTO.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressRequest {
    private String zipcode;
    private String address;
    private String addressDetail;
}