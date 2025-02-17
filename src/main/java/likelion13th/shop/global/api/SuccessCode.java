package likelion13th.shop.global.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessCode implements BaseCode { // 성공
    // Common
    OK(HttpStatus.OK, "COMMON_200", "Success"),
    CREATED(HttpStatus.CREATED, "COMMON_201", "Created"),

    // User
    USER_LOGIN_SUCCESS(HttpStatus.CREATED, "USER_201", "회원가입& 로그인이 완료되었습니다."),
    USER_LOGOUT_SUCCESS(HttpStatus.OK, "USER_200", "로그아웃 되었습니다."),
    USER_REISSUE_SUCCESS(HttpStatus.OK, "USER_200", "토큰 재발급이 완료되었습니다."),
    USER_DELETE_SUCCESS(HttpStatus.OK, "USER_200", "회원탈퇴가 완료되었습니다."),

    // Category
    CATEGORY_ITEMS_GET_SUCCESS(HttpStatus.OK,"CATEGORY_2001", "카테고리 상품 조회 성공"),
    CATEGORY_ITEMS_EMPTY(HttpStatus.OK, "CATEGORY_204", "해당 카테고리에 등록된 상품이 없습니다."),

    // Item
    ITEM_GET_SUCCESS(HttpStatus.OK, "ITEM_2003", "상품 조회에 성공했습니다."),

    // Order
    ORDER_CREATE_SUCCESS(HttpStatus.CREATED, "ORDER_201", "주문이 성공적으로 생성되었습니다."),
    ORDER_GET_SUCCESS(HttpStatus.OK, "ORDER_2001", "주문 조회에 성공했습니다."),
    ORDER_LIST_SUCCESS(HttpStatus.OK, "ORDER_2002", "모든 주문 목록 조회에 성공했습니다."),
    ORDER_CANCEL_SUCCESS(HttpStatus.OK, "ORDER_2003", "주문이 성공적으로 취소되었습니다."),

    // S3
    S3_UPLOAD_SUCCESS(HttpStatus.OK, "S3_200", "S3 업로드가 성공적으로 완료되었습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    // 응답 코드 상세 정보 return
    @Override
    public ReasonDto getReason() {
        return ReasonDto.builder()
                .httpStatus(this.httpStatus)
                .code(this.code)
                .message(this.message)
                .build();
    }
}
