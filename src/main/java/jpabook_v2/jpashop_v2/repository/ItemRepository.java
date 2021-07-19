package jpabook_v2.jpashop_v2.repository;

import jpabook_v2.jpashop_v2.domain.Item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
    private final EntityManager em;

    public void save(Item item){
        //JPA에 저장하기 전까지는 id값이 없다.새로 생성하는 객체라는 뜻
        if(item.getId() == null){//처음에는 id라는 게 없기 때문에.
            em.persist(item);
        } else{//이미 DB에 등록된 것이라는 뜻.
            em.merge(item);//ItemService의 updateItem와 동일. 파라미터의 값으로 영속성 엔티티를 조회해서 가져온 것을 다 바꾼다!
            //1.파라미터로 들어간 item은 영속성 컨텍스트으로 변하지 않고, 2.em.merge(item)의 결과가 영속성 컨텍스트에서 관리되는 객체다! (1 != 2) 쓸거면 2를 써야함.
        }
    }
    public Item findOne(Long id){
        return em.find(Item.class,id);
    }
    public List<Item> findAll(){
        return em.createQuery("select i from Item i",Item.class)
                .getResultList();
    }
}
