package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import study.datajpa.entity.Item;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;


    @Test
    public void save() {
        Item item = new Item("A"); // em.persist() 하기 전까지 id값은 null이다. persist하고나서 값 할당
        itemRepository.save(item);
    }

}