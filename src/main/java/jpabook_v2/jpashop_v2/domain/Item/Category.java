package jpabook_v2.jpashop_v2.domain.Item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
public class Category {
    @Id @GeneratedValue
    @Column(name="category_id")

    private String name;

    @ManyToMany//중간에 category_item 테이블과 조인!!!
    @JoinTable(name="category_item",
        joinColumns = @JoinColumn(name="category_id"),
        inverseJoinColumns = @JoinColumn(name="item_id"))
    private List<Item> items = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();

    //==연관관계 메서드==//
    public void addChildCategory(Category child) {//셀프 양방향
        this.child.add(child);//현재 본인의 자식 컬렉션에도 들어가야하고
        child.setParent(this);//자식의 부모에도 들어가야한다.
    }
}
