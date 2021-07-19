package jpabook_v2.jpashop_v2.repository;

import jpabook_v2.jpashop_v2.domain.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderSearch {
    private String memberName;//회원 이름
    private OrderStatus orderStatus;//주문 상태[ORDER, CANCEL]
}
