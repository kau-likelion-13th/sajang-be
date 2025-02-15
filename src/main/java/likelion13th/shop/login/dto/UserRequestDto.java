package likelion13th.shop.login.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserRequestDto {

    @Schema(description = "UserReqDto")
    @Getter
    @Builder
    @AllArgsConstructor
    public static class UserReqDto {
        private Long userId;      // user_id (Long 타입)
        private String providerId;// 카카오 provider (필수)
        private String usernickname;  // 닉네임
    }
}
