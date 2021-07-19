package jpabook_v2.jpashop_v2.controller;

import jpabook_v2.jpashop_v2.domain.Item.Item;
import jpabook_v2.jpashop_v2.domain.Member;
import jpabook_v2.jpashop_v2.domain.Order;
import jpabook_v2.jpashop_v2.repository.OrderSearch;
import jpabook_v2.jpashop_v2.service.ItemService;
import jpabook_v2.jpashop_v2.service.MemberService;
import jpabook_v2.jpashop_v2.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping("/order")
    public String createForm(Model model){
        List<Member> members = memberService.findMembers();
        List<Item> items = itemService.findItems();

        model.addAttribute("members",members);
        model.addAttribute("items",items);

        return "order/orderForm";
    }
    @PostMapping("/order")
    public String order(@RequestParam("memberId") Long memberId,
                        @RequestParam("itemId") Long itemId,
                        @RequestParam("count") int count){
        orderService.order(memberId,itemId,count);//상품 여러개를 선택할 땐 이부분 고쳐주면 됨.
//        Long orderId = orderService.order(memberId, itemId, count);//상품 여러개를 선택할 땐 이부분 고쳐주면 됨.

        return "redirect:/orders";
//        return "redirect/orders"+orderId;//주문된 결과 페이지로 리다이렉트
    }
    @GetMapping("/orders")
    //form 태그로 HTML Form 데이터들 전송하면 값이 있는 상태로 OrderSearch가 넘겨진다!
    //GetMapping으로  요청파라미터 데이터들 파라미터 바인딩한다!!
    //@ModelAttribute에 "orderSearch"라는 이름으로 모델에 OrderSearch 담아준다!
    public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model){
        List<Order> orders = orderService.findOrders(orderSearch);
        model.addAttribute("orders",orders);

        return "order/orderList";
    }
    @PostMapping("/orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable("orderId") Long orderId){
        orderService.cancelOrder(orderId);
        return "redirect:/orders";//주문 목록으로 리다이렉트
    }
}
