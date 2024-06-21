package com.example.itemservice.repository;

import com.example.itemservice.domain.item.Category;
import com.example.itemservice.domain.item.Item;
import org.springframework.data.domain.Page;

public interface ItemRepositoryCustom {

    Page<Item> getPage(String search, Category category, int page);
}
