package likelion13th.shop.login.mapper;

import likelion13th.shop.login.auth.dto.JwtDto;

public class UserConverter {

    // ✅ JWT 토큰 DTO 변환
    public static JwtDto jwtDto(String access, String refresh, String signIn) {
        return JwtDto.builder()
                .accessToken(access)
                .refreshToken(refresh)
                .build();
    }
}
