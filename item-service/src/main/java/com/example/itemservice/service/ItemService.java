package com.example.itemservice.service;

import com.example.itemservice.domain.item.Item;
import com.example.itemservice.repository.ItemRepository;
import com.example.itemservice.request.ItemQuantity;
import com.example.itemservice.request.ItemRequest;
import com.example.itemservice.response.ItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;


    public List<ItemResponse> items() {

        return itemRepository.findAll().stream().map(ItemResponse::new).toList();

    }

    public void addItem(ItemRequest itemRequest) {

        itemRepository.save(
                Item.builder()
                        .name(itemRequest.getName())
                        .itemDescription(itemRequest.getItemDescription())
                        .price(itemRequest.getPrice())
                        .stock(itemRequest.getStock())
                        .category(itemRequest.getCategory())
                        .itemUUID(UUID.randomUUID().toString())
                        .build()
        );

    }


    public void deleteItem(String itemId) {

        Item item = itemRepository.findByItemUUID(itemId).orElseThrow(() -> new RuntimeException("상품 존재 x"));

        itemRepository.delete(item);

    }


    public void reduceQuantity(List<ItemQuantity> quantities) {

        for (ItemQuantity quantity : quantities) {
            Item item = itemRepository.findByItemUUID(quantity.getItemUUID()).orElseThrow(() -> new RuntimeException("아이템 없음"));
            item.reduceQuantity(quantity.getQuantity());
        }

    }

    public void addQuantity(List<ItemQuantity> quantities) {

        for (ItemQuantity quantity : quantities) {
            Item item = itemRepository.findByItemUUID(quantity.getItemUUID()).orElseThrow(() -> new RuntimeException("아이템 없음"));
            item.addQuantity(quantity.getQuantity());
        }
    }
}
