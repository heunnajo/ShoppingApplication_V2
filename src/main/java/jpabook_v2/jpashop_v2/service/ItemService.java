package jpabook_v2.jpashop_v2.service;

import jpabook_v2.jpashop_v2.domain.Item.Item;
import jpabook_v2.jpashop_v2.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)//읽기 에서 약간의 성능 향상 가능
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    @Transactional//꼭 넣어줘야함!! saveIte는 쓰기인데 readOnly이면 읽기 전용이라 저장 안됨!
    public void saveItem(Item item){
        itemRepository.save(item);
    }

    @Transactional
    //public void updateItem(Long itemId,UpdateItmeDto dto){
    public void updateItem(Long id,String name, int price){
        //준영속 엔티티 수정방법1. 변경 감지 기능 사용
        //id를 기반으로 실제 엔티티를 가져온다.
        //값을 셋팅하고 나면 스프링의 @Transactional에 의해 이 트랜잭션이 커밋된다.
        //커밋하면 JPA는 flush()한다.영속성 컨텍스트 중 변경된 것들 다 찾는다!
        //그러면 findItem이라는 엔티티의 값이 set으로 변경된 것을 감지해서 커밋될 때 DB에 업데이트한다!
        Item item = itemRepository.findOne(id);
        item.setName(name);
        item.setPrice(price);
    }
    public List<Item> findItems(){
        return itemRepository.findAll();
    }
    public Item findOne(Long itemId){
        return itemRepository.findOne(itemId);
    }
}
