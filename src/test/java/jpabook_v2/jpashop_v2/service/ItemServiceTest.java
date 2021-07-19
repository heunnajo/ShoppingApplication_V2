package jpabook_v2.jpashop_v2.service;

import jpabook_v2.jpashop_v2.domain.Item.Book;
import jpabook_v2.jpashop_v2.domain.Item.Item;
import jpabook_v2.jpashop_v2.repository.ItemRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ItemServiceTest {
    @Autowired ItemService itemService;
    @Autowired ItemRepository itemRepository;

    @Test
    public void 상품등록() throws Exception{
        //given
        Item item = new Book();

        //when : 생성한 item의 id와 itemRepository에서 가져온 item id 동일한지 검사.
        itemRepository.save(item);

        //then
        assertEquals(item,itemRepository.findOne(item.getId()));
    }
}