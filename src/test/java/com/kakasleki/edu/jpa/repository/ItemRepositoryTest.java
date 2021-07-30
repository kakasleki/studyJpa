package com.kakasleki.edu.jpa.repository;

import com.kakasleki.edu.jpa.entity.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;

    /*@Test
    public void save() {
        Item item = new Item();
        this.itemRepository.save(item);
    }*/
}
