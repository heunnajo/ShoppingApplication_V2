package jpabook_v2.jpashop_v2.api;

import jpabook_v2.jpashop_v2.repository.OrderRepository;
import jpabook_v2.jpashop_v2.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.Order;
import java.util.List;

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

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1(){//EAGER로 변경시 : findAllByString : JPQL은 그대로 SQL로 번역. =>Order만 가져오는 쿼리인데 EAGER로 되있으면 연관된 엔티티 모두 다 조회한다!!(N+1문제 발생), 다른 API에서 불필요한 데이터들도 끌고 온다!
        List<javax.persistence.criteria.Order> all = orderRepository.findAllByCriteria(new OrderSearch());//List<Order>
        return all;//
        for (javax.persistence.criteria.Order order : all) {
            order.getMember().getName();//Lazy 강제 초기화
            order.getDelivery().getAddress();//Lazy 강제 초기화
        }
    }

}
