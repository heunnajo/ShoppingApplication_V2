package jpabook_v2.jpashop_v2.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {
    @Id @GeneratedValue
    @Column(name="member_id")
    private Long id;//PK

    @NotEmpty
    private String name;
    @Embedded
    private Address address;

    @JsonIgnore
    @OneToMany(mappedBy = "member")//Order 테이블의 member 필드에 의해 매핑된 것이라는 뜻!(읽기 전용)
    private List<Order> orders = new ArrayList<>();
}
