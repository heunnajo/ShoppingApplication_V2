package jpabook_v2.jpashop_v2.repository.order.simplequery;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {

    private final EntityManager em;
    public List<OrderSimpleQueryDto> findOrderDtos(){
        return em.createQuery("select new jpabook_v2.jpashop_v2.repository.order.simplequery.OrderSimpleQueryDto(o.id, m.name," +
                "o.orderDate, o.status, d.address) " +
                "From Order o" +
                " join o.member m" +
                " join o.delivery d", OrderSimpleQueryDto.class)
                .getResultList();
    }

}
