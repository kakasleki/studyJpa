package com.kakasleki.edu.jpa.repository;

import com.kakasleki.edu.jpa.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
