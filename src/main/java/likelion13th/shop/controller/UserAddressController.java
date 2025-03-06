package likelion13th.shop.controller;

import likelion13th.shop.DTO.request.AddressRequest;
import likelion13th.shop.DTO.response.AddressResponse;
import likelion13th.shop.service.UserAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/address")
@RequiredArgsConstructor
public class UserAddressController {

    private final UserAddressService userAddressService;

    // ✅ 주소 저장 API (POST /users/address)
    @PostMapping
    public ResponseEntity<AddressResponse> saveAddress(
            @RequestBody AddressRequest request,
            @RequestHeader("X-USER-ID") String providerId) {
        AddressResponse addressResponse = userAddressService.saveAddress(providerId, request);
        return ResponseEntity.ok(addressResponse);
    }

    // ✅ 주소 조회 API (GET /users/address)
    @GetMapping
    public ResponseEntity<AddressResponse> getAddress(@RequestHeader("X-USER-ID") String providerId) {
        AddressResponse addressResponse = userAddressService.getAddress(providerId);
        return ResponseEntity.ok(addressResponse);
    }
}