package jpabook_v2.jpashop_v2.repository.order.query;

import lombok.Data;

@Data
public class OrderItemQueryDto {//주문 상품명, 주문 가격, 주문 갯수

    private Long orderId;
    private String itemName;
    private int orderPrice;
    private int count;

    public OrderItemQueryDto(Long orderId, String itemName, int orderPrice, int count) {
        this.orderId = orderId;
        this.itemName = itemName;
        this.orderPrice = orderPrice;
        this.count = count;
    }
}
