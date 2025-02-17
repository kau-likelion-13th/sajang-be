package likelion13th.shop.login.auth.utils;

import likelion13th.shop.global.api.ErrorCode;
import likelion13th.shop.domain.User;
import likelion13th.shop.login.dto.UserRequestDto.UserReqDto;
import likelion13th.shop.login.service.UserService;
import likelion13th.shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * ✅ 카카오 OAuth2 유저 서비스
 * - provider_id 기반으로 회원 확인 및 신규 회원 처리
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2UserServiceImpl extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final UserService userService;

    /**
     * ✅ 카카오 로그인 유저 정보 조회 및 회원 처리 (provider_id 기반)
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("// ✅ 카카오 OAuth2 로그인 시도");

        try {
            // 🔹 카카오 유저 정보 추출
            Map<String, Object> attributes = oAuth2User.getAttributes();
            Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
            String nickname = properties != null ? (String) properties.get("nickname") : "카카오사용자";

            // ✅ 1️⃣ 카카오 고유 식별자 (provider_id: String)
            String providerId = attributes.get("id").toString();

            // 🔹 provider_id 기반 회원 여부 확인 및 신규 회원 처리
            Optional<User> userOptional = userRepository.findByProviderId(providerId);
            userOptional.orElseGet(() -> {
                UserReqDto userReqDto = new UserReqDto(
                        null,           // user_id (자동 생성)
                        providerId,     // 카카오 고유 provider_id
                        nickname        // 카카오 닉네임
                );
                log.info("// 🆕 UserService.createUser() 호출 (providerId: {}, nickname: {})", providerId, nickname);
                return userService.createUser(userReqDto);
            });

            // ✅ 3️⃣ OAuth2User 반환 (provider_id 포함)
            Map<String, Object> extendedAttributes = new HashMap<>(attributes);
            extendedAttributes.put("provider_id", providerId); // ✅ provider_id 포함

            return new DefaultOAuth2User(
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                    extendedAttributes,
                    "provider_id" // ✅ "provider_id"를 key로 설정
            );

        } catch (Exception e) {
            log.error("// ❌ 카카오 OAuth2 처리 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException(ErrorCode.USER_NOT_FOUND.getMessage());
        }
    }
}
