package jpabook_v2.jpashop_v2.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
public class Delivery {
    @Id @GeneratedValue
    private Long id;

    @JsonIgnore
    @OneToOne(mappedBy = "delivery", fetch = LAZY)
    private Order order;

    @Embedded
    private Address address;

//    @Enumerated(EnumType.ORDINAL)//ORDINAL은 숫자로 들어간다!아래 같은 이유 때문에 절대 쓰면 안됨.
    @Enumerated(EnumType.STRING)
    private DelieveryStatus status;//READY, COMP=>1,0으로 한다고 해도 중간에 다른 상태가 들어가버리면 숫자가 꼬이면서 데이터도 꼬이게 된다.
}
