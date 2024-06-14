package com.example.itemservice.repository;

import com.example.itemservice.domain.item.Category;
import com.example.itemservice.domain.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item,Long> {
    Optional<Item> findByItemUUID(String itemId);

    List<Item> findAllByCategory(Category category);

}
