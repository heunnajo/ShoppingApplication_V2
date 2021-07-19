package jpabook_v2.jpashop_v2.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jpabook_v2.jpashop_v2.domain.Item.Item;
import jpabook_v2.jpashop_v2.domain.Item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {
    @Id @GeneratedValue
    @Column(name="order_item_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="item_id")
    private Item item;//Item과 OrderItem과 관계 매핑!

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="order_id")
    private Order order;//FK가 된다. order와 매핑 :다대일(ManytoOne). 하나의 order는 여러개의 orderitem을 가질 수 있다!

    private int orderPrice;//주문 가격
    private int count;//주문 수량


    //==생성 메서드==//
    //orderItem 객체 생성하면서 count만큼 재고 수량 감산.
    //item과 orderPrice 따로 받는다! 할인이벤트 적용 같은 것이 있을 수 있기 때문.
    public static OrderItem createOrderItem(Item item, int orderPrice, int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count);
        return orderItem;
    }

    //==비즈니스 로직==//
    //OrderItem 안에 Item이 있으므로 item의 재고를 늘린다!
    public void cancel(){
        getItem().addStock(count);//재고 수량 증가.아이템을 가져와서(getItem()) 주문 수량만큼 다시 늘려줘야한다!
    }
    //==조회 로직==//

    /**
     * 주문 상품 전체 가격 조회
     */
    public int getTotalPrice(){//주문 가격 * 주문 수량
        return getOrderPrice() * getCount();
    }
}
