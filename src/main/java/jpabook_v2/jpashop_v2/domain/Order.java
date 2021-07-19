package jpabook_v2.jpashop_v2.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

//관례로 order가 되버리는 것을 방지하기 위해 반드시 @Table 애노테이션을 붙여준다!
@Entity
@Table(name="orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {
    @Id @GeneratedValue
    @Column(name="order_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="member_id")
    private Member member;//(FK)Member-Order 연관관계 주인.

    @OneToMany(mappedBy = "order",cascade=CascadeType.ALL)//orderItem의 order에 의해 매핑된다!
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = LAZY, cascade=CascadeType.ALL)//LAZY로 하지않으면 delivery 조회되는 순간 delivery order 찾고 난리남. JPA 성능 저하.
    @JoinColumn(name="delivery_id")//원래는 delivery, order 각각 persist해줘야하지만
    private Delivery delivery;//캐스케이드 추가하면 order 저장할 때 delivery도 같이 persist

    private LocalDateTime orderDate;//주문시간. Date를 쓰면 애노테이션 달아야하지만 자바 LDT 이용하면 Hibernate가 자동 지원.

    @Enumerated(EnumType.STRING)
    private OrderStatus status;//enum 타입으로 주문상태 [ORDER,CANCEL]

    //==연관관계 편의 메서드==//
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //==생성 메서드==//
    //생성하면서 연관관계가 얽힌 것들을 한번에 같이 해주기 때문에 주문 생성 후에 변경할 것이 있으면 createOrder만 변경해주면 된다!
    //이전에도 말했다시피 외부에서 set하는 것이 아니라 생성메서드에서 한번에 처리한다.
    public static Order createOrder(Member member,Delivery delivery,OrderItem... orderItems){//orderItem에서 이미 수량 감산하고 넘어온다.
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for(OrderItem orderItem : orderItems){
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //==비즈니스 로직==//
    /**
     * 주문 취소 : 배송 완료되면 주문 취소가 불가능! 배송상태(DeliveryStatus)가 현재 Order 엔티티 안에 있기 때문에 Order 클래스 내부에 구현해준다!
     */
    public void cancel(){
        //validation 로직
        if(delivery.getStatus() == DelieveryStatus.COMP){
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다");
        }
        //validation 통과 후 : 현재 주문 상태를 CANCEL로하고, 주문 상품도 원상 복구해준다!!
        this.setStatus(OrderStatus.CANCEL);
        for(OrderItem orderItem : orderItems){
            orderItem.cancel();//주문(상품 2개)일 때 상품 각각에 대해서도 취소처리해줘야함
        }
    }
    //==조회 로직==//
    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice(){//stream을 mapToInt로 변환
//        return orderItems.stream()
//                .mapToInt(OrderItem::getTotalPrice)
//                .sum();
        int totalPrice = 0;
        for(OrderItem orderItem : orderItems){
            totalPrice += orderItem.getTotalPrice();//300*5 + 400*3 + ...
        }
        return totalPrice;
    }
}
