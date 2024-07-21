package com.example.itemservice.repository;

import com.example.itemservice.domain.item.Category;
import com.example.itemservice.domain.item.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ItemRepository extends JpaRepository<Item,Long>,ItemRepositoryCustom {
    Optional<Item> findByItemUUID(String UUID);

    List<Item> findAllByCategory(Category category);

    @Query("SELECT i FROM Item i WHERE i.itemUUID IN :UUIDs")
    List<Item> findItemInUUIDs(Set<String> UUIDs);

}
