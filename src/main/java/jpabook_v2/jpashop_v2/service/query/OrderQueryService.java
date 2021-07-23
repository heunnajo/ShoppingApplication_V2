//package jpabook_v2.jpashop_v2.service.query;
//
//import jpabook_v2.jpashop_v2.api.OrderApiController;
//import jpabook_v2.jpashop_v2.domain.Order;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.bind.annotation.GetMapping;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Transactional(readOnly = true)
//public class OrderQueryService {
//    public List<OrderApiController.OrderDto> ordersV3() {
//        List<Order> orders = orderRepository.findAllWithItem();
//
//        for (Order order : orders) {
//            System.out.println("order ref = " + order + "order.id = " + order.getId());
//        }
//
//        List<OrderApiController.OrderDto> result = orders.stream()
//                .map(o -> new OrderApiController.OrderDto(o))
//                .collect(Collectors.toList());
//        return result;
//    }
//}
