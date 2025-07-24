package likelion13th.shop.global.constant;

//배송 중, 배송 완료, 주문 취소
public enum OrderStatus {
    PROCESSING("처리중"),
    COMPLETE("완료"),
    CANCEL("취소");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }
}