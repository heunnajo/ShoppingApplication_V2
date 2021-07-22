package jpabook_v2.jpashop_v2.repository.order.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager em;


    public List<OrderQueryDto> findOrderQueryDtos(){
        List<OrderQueryDto> result = findOrders();
        result.forEach(o -> {
            List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId());//컬렉션은 제외하고 직접 채우고 있다.
            o.setOrderItems(orderItems);
        });
        return result;
    }

    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery("select new jpabook_v2.jpashop_v2.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)"+
                " from OrderItem oi"+
                " join oi.item i"+
                " where oi.order.id = : orderId", OrderItemQueryDto.class)
                .setParameter("orderId",orderId)
                .getResultList();
    }

    private List<OrderQueryDto> findOrders() {
        return em.createQuery(
                "select new jpabook_v2.jpashop_v2.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                        " from Order o" +
                        " join o.member m" +
                        " join o.delivery d", OrderQueryDto.class)
                .getResultList();
    }

    public List<OrderQueryDto> findAllByDto_optimization() {
        List<OrderQueryDto> result = findOrders();//루트 조회
        Map<Long, List<OrderItemQueryDto>> orderItemMap = findOrderItemMap(toOrderIds(result));//아래 2줄을 한줄로 리팩토링. 주문 데이터를 한번에 map으로 메모리에 올린다!!!!!!
//        List<Long> orderIds = toOrderIds(result);
//        Map<Long, List<OrderItemQueryDto>> orderItemMap = findOrderItemMap(orderIds);

        result.forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));
        //차이점 - 이전 : foreach 돌 때마다 쿼리 날렸지만, 이제는 메모리에 데이터 다 가져온뒤, 메모리에서 값을 매칭해서 값 셋팅한다!
        //총 쿼리 갯수 2개 : 1.findOrders(), 2. em.createQuery
        return result;

    }

    private Map<Long, List<OrderItemQueryDto>> findOrderItemMap(List<Long> orderIds) {
        List<OrderItemQueryDto> orderItems = em.createQuery("select new jpabook_v2.jpashop_v2.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                " from OrderItem oi" +
                " join oi.item i" +
                " where oi.order.id in :orderIds", OrderItemQueryDto.class)
                .setParameter("orderIds", orderIds)//findOrders()로 가져온 List<OrderQueryDto>를 stream, map으로 돌려서 id를 다 뽑는다!
                .getResultList();

        //이정도 해도 되지만 코드도 간결, 성능최적화를 위해 map으로 변환한다!
        //orderItemQueryDto.getOrderId() 기준으로 map으로 바꿀 수 있다!
        //key : orderItemQueryDto.getOrderId()
        //value : List<OrderItemQueryDto>
        Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItems.stream()
                .collect(Collectors.groupingBy(orderItemQueryDto -> orderItemQueryDto.getOrderId()));
        return orderItemMap;
    }

    private List<Long> toOrderIds(List<OrderQueryDto> result) {
        List<Long> orderIds = result.stream()
                .map(o -> o.getOrderId())//stream, map으로 orderId의 리스트가 된다! 주문번호 4번, 11번 존재할 것이다!
                .collect(Collectors.toList());
        return orderIds;
    }

    public List<OrderFlatDto> findAllByDto_flat() {
        return  em.createQuery(
                "select new jpabook_v2.jpashop_v2.repository.order.query.OrderFlatDto(o.id, m.name, o.orderDate, o.status, d.address, i.name, oi.orderPrice, oi.count)" +
                        " from Order o" +
                        " join o.member m" +
                        " join o.delivery d" +
                        " join o.orderItems oi" +
                        " join oi.item i", OrderFlatDto.class)
                .getResultList();
    }
}
