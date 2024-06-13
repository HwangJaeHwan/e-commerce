package com.example.itemservice.service;

import com.example.itemservice.domain.Item;
import com.example.itemservice.repository.ItemRepository;
import com.example.itemservice.request.ItemRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;


    public void addItem(ItemRequest itemRequest) {

        itemRepository.save(new Item(itemRequest.getName(), itemRequest.getItemDescription(), itemRequest.getPrice()));

    }

}
