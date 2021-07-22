package jpabook_v2.jpashop_v2.repository;

import jpabook_v2.jpashop_v2.domain.Member;
import jpabook_v2.jpashop_v2.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    @PersistenceContext
    EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {

        return em.find(Order.class, id);
    }

    public List<Order> findAllByCriteria(OrderSearch orderSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Order, Member> m = o.join("member", JoinType.INNER); //회원과 조인
        List<Predicate> criteria = new ArrayList<>();
//주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"),
                    orderSearch.getOrderStatus());
            criteria.add(status);
        }
//회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name =
                    cb.like(m.<String>get("name"), "%" +
                            orderSearch.getMemberName() + "%");
            criteria.add(name);

        }
        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000); //최대 1000건
        return query.getResultList();
    }

    public List<Order> findAllWithMemberDelivery() {
        return em.createQuery(
                "select o from Order o" +
                        " join fetch o.member m" +
                        " join fetch o.delivery d", Order.class
        ).getResultList();
    }

    public List<Order> findAllWithItem() {
        return em.createQuery(
                "select distinct o from Order o" +
                        " join fetch o.member m" +
                        " join fetch o.delivery d" +
                        " join fetch o.orderItems oi" +
                        " join fetch oi.item i", Order.class)//order와 orderItems를 조인! ex) 주문 한건에 주문상품 4개면 조인하면 결과는 4개가 된다.
                .getResultList();
    }
}
/*
public class OrderRepository {
    private final EntityManager em;

    public void save(Order order){
        em.persist(order);
    }

    public Order findOne(Long id){
        return em.find(Order.class,id);
    }

    //검색 기능
    public List<Order> findAll(OrderSearch orderSearch){
        //order를 먼저 조회하고, member와 조인.
        //order와 연관된 member 조인한다.

        //동적 쿼리 작성 방법1.
        String jpql = "select o from Order o join o.member m";
        boolean isFirstCondition = true;

        //JPQL 동적 빌드
        //주문 상태 검색
        if(orderSearch.getOrderStatus() != null){
            if(isFirstCondition){
                jpql += "where";
                isFirstCondition = false;
            } else {
                jpql += "and";
            }
            jpql += "o.status = :status";
        }
        //회원 이름
        if(StringUtils.hasText(orderSearch.getMemberName())){
            if(isFirstCondition){
                jpql += "where";
                isFirstCondition = false;
            } else {
                jpql += "and";
            }
            jpql += "o.status = :status";
        }

        TypedQuery<Order> query = em.createQuery(jpql, Order.class)
                .setMaxResults(1000);

        //동적 파라미터 바인딩
        if(orderSearch.getOrderStatus() != null){
            query = query.setParameter("status",orderSearch.getOrderStatus());
        }
        if(StringUtils.hasText(orderSearch.getMemberName())){
            query = query.setParameter("name",orderSearch.getMemberName());
        }
        return query.getResultList();
    }
}*/
