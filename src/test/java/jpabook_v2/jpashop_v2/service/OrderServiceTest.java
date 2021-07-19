package jpabook_v2.jpashop_v2.service;

import jpabook_v2.jpashop_v2.domain.Address;
import jpabook_v2.jpashop_v2.domain.Item.Book;
import jpabook_v2.jpashop_v2.domain.Item.Item;
import jpabook_v2.jpashop_v2.domain.Member;
import jpabook_v2.jpashop_v2.domain.Order;
import jpabook_v2.jpashop_v2.domain.OrderStatus;
import jpabook_v2.jpashop_v2.exception.NotEnoughStockException;
import jpabook_v2.jpashop_v2.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {
    
    //테스트 데이터를 넣는 것이 목적이기 때문에 EntityManager 바로 받아온다.
    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;
    /**상품 주문 테스트*/
    @Test
    public void 상품주문() throws Exception{
        //given
        Member member = createMembr();
        Item book = createBook("시골 JPA", 10000, 10);

        //when : 주문서비스로 주문 생성
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then : 검증=>orderRepository에 주문이 잘 들어가 있는지 확인!
        //1. 상품 주문 상태
        //예상되는 결과 : 주문 상태는 ORDER
        //실제 값 : getOrder(주문 id로 order리포지토리에서 찾은 주문)의 상태
        //2. 주문 상품 갯수
        //Order의 List<OrderItem>인 orderItems의 크기 확인!
        //3.계산 로직 검증
        //4.주문 수량만큼 재고 수량 감소
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER,getOrder.getStatus());
        assertEquals("주문한 상품 종류 수가 정확해야 한다", 1,getOrder.getOrderItems().size());
        assertEquals("주문 가격은 가격 * 수량이다.",10000*orderCount,getOrder.getTotalPrice());
        assertEquals("주문 수량만큼 재고가 줄어야 한다.",8,book.getStockQuantity());
    }


    /**상품주문_재고수량초과 테스트*/
    @Test(expected = NotEnoughStockException.class)
    public void 상품주문_재고수량초과() throws Exception{
        //given
        Member member = createMembr();
        Book book = createBook("시골 JPA", 10000, 10);

        int orderCount = 11;
        //when : 재고 수량볻다 많은 주문수량이면 Exception 터져야함!
        orderService.order(member.getId(), book.getId(),orderCount);
        //then
        fail("재고 수량 부족 예외가 발생해야 한다.");//테스트 코드를 잘못 작성했다는 뜻.
    }
    /**상품 주문 취소 테스트*/
    @Test
    public void 주문취소() throws Exception{
        //given
        Member member = createMembr();
        Book item = createBook("시골 JPA",10000,10);

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);
        //when : 실제 테스트하고 싶은 상황
        orderService.cancelOrder(orderId);
        //then
        //1. 주문 상태 CANCEL인지 확인
        //2. 재고 정상 복구 확인
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals("주문 취소 시 상태는 CANCEL이다.",OrderStatus.CANCEL,getOrder.getStatus());
        assertEquals("주문 취소된 상품은 그만큼 재고가 증가해야 한다.",10,item.getStockQuantity());//캔슬하기 때문에 수량 다시 10개 되야함.
    }
    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();

        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMembr() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울","강가","123-123"));
        em.persist(member);
        return member;
    }
}