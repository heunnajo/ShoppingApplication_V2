package jpabook_v2.jpashop_v2.api;

import jpabook_v2.jpashop_v2.domain.Address;
import jpabook_v2.jpashop_v2.domain.Order;
import jpabook_v2.jpashop_v2.domain.OrderStatus;
import jpabook_v2.jpashop_v2.repository.OrderRepository;
import jpabook_v2.jpashop_v2.repository.OrderSearch;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

//조회 시 다음과 같은 연관관계 존재

/**
 * xToOne
 * Order
 * Order -> Member : ManyToOne
 * Order -> Delivery : OneToOne
 */

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;

//    @GetMapping("/api/v1/simple-orders")
//    public List<Order> ordersV1(){//EAGER로 변경시 : findAllByString : JPQL은 그대로 SQL로 번역. =>Order만 가져오는 쿼리인데 EAGER로 되있으면 연관된 엔티티 모두 다 조회한다!!(N+1문제 발생), 다른 API에서 불필요한 데이터들도 끌고 온다!
//        List<Order> all = orderRepository.findAllByCriteria(new OrderSearch());//List<Order>
//        return all;//
//        for (Order order : all) {
//            order.getMember().getName();//Lazy 강제 초기화
//            order.getDelivery().getAddress();//Lazy 강제 초기화
//        }
//    }

    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2(){
        //ORDER 2개
        List<Order> orders = orderRepository.findAllByCriteria(new OrderSearch());
        //루프
        List<SimpleOrderDto> result = orders.stream()//Order를 DTO로 변환!
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());
        return result;
    }

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate; //주문시간 private OrderStatus orderStatus;
        private OrderStatus orderStatus;
        private Address address;
        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();//LAZY 초기화! 영속성 컨텍스트 조회해서 없으면 DB MEMBER 조회 쿼리 날린다!
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();//LAZY 초기화! 영속성 컨텍스트 조회해서 없으면 DB DELIVERY 조회 쿼리 날린다!
        }
    }

}
