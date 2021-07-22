package jpabook_v2.jpashop_v2.repository.order.query;

import jpabook_v2.jpashop_v2.domain.Address;
import jpabook_v2.jpashop_v2.domain.OrderStatus;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data @Getter
public class OrderFlatDto {
    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;

    private String itemName;
    private int orderPrice;
    private int count;

    public OrderFlatDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address, String itemName, int orderPrice, int count) {
        this.orderId = orderId;
        this.name = name;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address;
        this.itemName = itemName;
        this.orderPrice = orderPrice;
        this.count = count;
    }

}
