package likelion13th.shop.service;

import likelion13th.shop.domain.User;
import likelion13th.shop.domain.Address;
import likelion13th.shop.DTO.request.AddressRequest;
import likelion13th.shop.DTO.response.AddressResponse;
import likelion13th.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserAddressService {

    private final UserRepository userRepository;

    // ✅ 주소 저장 (신규 입력 or 업데이트)
    @Transactional
    public AddressResponse saveAddress(String providerId, AddressRequest request) {
        User user = userRepository.findByProviderId(providerId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 새로운 주소 설정
        Address newAddress = new Address(
                request.getZipcode(),
                request.getAddress(),
                request.getAddressDetail()
        );
        user.updateAddress(newAddress); // User 엔티티에 주소 업데이트

        userRepository.save(user); // 변경 사항 저장

        return new AddressResponse(user.getAddress());
    }

    // ✅ 주소 조회
    @Transactional(readOnly = true)
    public AddressResponse getAddress(String providerId) {
        User user = userRepository.findByProviderId(providerId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        return new AddressResponse(user.getAddress());
    }
}