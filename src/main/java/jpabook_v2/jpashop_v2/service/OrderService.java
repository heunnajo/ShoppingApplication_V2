package jpabook_v2.jpashop_v2.service;

import jpabook_v2.jpashop_v2.domain.Delivery;
import jpabook_v2.jpashop_v2.domain.Item.Item;
import jpabook_v2.jpashop_v2.domain.Member;
import jpabook_v2.jpashop_v2.domain.Order;
import jpabook_v2.jpashop_v2.domain.OrderItem;
import jpabook_v2.jpashop_v2.repository.ItemRepository;
import jpabook_v2.jpashop_v2.repository.MemberRepository;
import jpabook_v2.jpashop_v2.repository.OrderRepository;
import jpabook_v2.jpashop_v2.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {//이것저것 의존 많이 한다!
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    /**
     * 주문
     */
    @Transactional//데이터 변경하는 것이기 때문에!
    //회원 ID, 상품 ID, 수량 필요.=>Member, Item Repository 필요!
    public Long order(Long memberId, Long itemId, int count){
        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());//실제로는 회원 배송지와 실제 배송지는 다르지만 예제 간소화를 위해

        //주문상품 생성 : OrderItem 생성자(메서드) 소환~!
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
        //주문 생성 : Order 생성자(메서드) 소환~!
        Order order = Order.createOrder(member, delivery, orderItem);
        //주문 저장
        orderRepository.save(order);//cascade를 적용함으로써 order를 persist하면 order 내부의 컬렉션들(orderitems)과 delivery도 함께 persist된다!
        return order.getId();
    }
    /**
     * 취소
     */
    @Transactional//데이터 변경 시에 쿼리 작성해서 DB에도 변경해야함.
    public void cancelOrder(Long orderId){
        //주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        //주문 취소
        order.cancel();
    }
    //검색
    public List<Order> findOrders(OrderSearch orderSearch){
        return orderRepository.findAllByCriteria(orderSearch);//단순위임한다.=>컨트롤러에서 리포지토리로 바로 불러도 괜찮다.단순 화면 조회같은 경우.
    }
}
