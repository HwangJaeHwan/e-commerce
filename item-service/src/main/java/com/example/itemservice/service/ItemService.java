package com.example.itemservice.service;

import com.example.itemservice.client.ImageServiceClient;
import com.example.itemservice.client.ReviewServiceClient;
import com.example.itemservice.domain.item.Category;
import com.example.itemservice.domain.item.Item;
import com.example.itemservice.repository.ItemRepository;
import com.example.itemservice.request.ItemQuantity;
import com.example.itemservice.request.ItemRequest;
import com.example.itemservice.response.ItemDetailResponse;
import com.example.itemservice.response.ItemResponse;
import com.example.itemservice.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ImageServiceClient imageServiceClient;
    private final ReviewServiceClient reviewServiceClient;

    @Transactional(readOnly = true)
    public PageResponse items(String search, Category category, int page) {

        Page<Item> items = itemRepository.getPage(search, category, page);

        return PageResponse.builder()
                .isFirst(items.isFirst())
                .isLast(items.isFirst())
                .reviews(items.map(ItemResponse::new).toList())
                .totalElement(items.getTotalElements())
                .totalPage(items.getTotalPages())
                .build();



    }
    @Transactional(readOnly = true)
    public ItemDetailResponse getItem(Long itemId) {

        Item item = itemRepository.findById(itemId).orElseThrow(() -> new RuntimeException("아이템 없음"));
        ItemDetailResponse response = new ItemDetailResponse(item);
        response.linkUrls(imageServiceClient.getURls(item.getItemUUID()));
        response.linkScore(reviewServiceClient.getAverageScore(item.getItemUUID()));

        return new ItemDetailResponse(item);


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
