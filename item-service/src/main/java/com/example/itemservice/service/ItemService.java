package com.example.itemservice.service;

import com.example.itemservice.client.ImageServiceClient;
import com.example.itemservice.client.ReviewServiceClient;
import com.example.itemservice.domain.item.Category;
import com.example.itemservice.domain.item.Item;
import com.example.itemservice.exception.ItemNotFoundException;
import com.example.itemservice.repository.ItemRepository;
import com.example.itemservice.request.CartItemInfo;
import com.example.itemservice.request.ItemQuantity;
import com.example.itemservice.request.ItemRequest;
import com.example.itemservice.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
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

        List<String> itemUUIDs = items.getContent().stream().map(Item::getItemUUID).toList();

        Map<String,Double> responses = reviewServiceClient.getAverageScores(itemUUIDs);


        return PageResponse.builder()
                .isFirst(items.isFirst())
                .isLast(items.isFirst())
                .items(items.map(item -> {
                    ItemResponse itemResponse = new ItemResponse(item);
                    itemResponse.setScore(responses.get(item.getItemUUID()));
                    return itemResponse;
                }).toList())
                .totalElement(items.getTotalElements())
                .totalPage(items.getTotalPages())
                .build();



    }
    @Transactional(readOnly = true)
    public ItemDetailResponse getItem(Long itemId) {

        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);
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

        Item item = itemRepository.findByItemUUID(itemId).orElseThrow(ItemNotFoundException::new);

        itemRepository.delete(item);

    }

    public BigDecimal amount(List<ItemQuantity> quantities) {
        int sum = 0;
        for (ItemQuantity quantity : quantities) {
            Item item = itemRepository.findByItemUUID(quantity.getItemUUID()).orElseThrow(ItemNotFoundException::new);
            sum += item.getPrice() * quantity.getQuantity();
        }

        return new BigDecimal(sum);

    }

    public List<CartItemResponse> getCartItems(Map<String,Integer> infos) {

        return itemRepository.findItemInUUIDs(infos.keySet()).stream().map(item -> {
            CartItemResponse cartItem = new CartItemResponse(item);
            cartItem.setQuantity(infos.get(item.getItemUUID()));
            cartItem.calItemPrice();
            return cartItem;
        }).toList();
    }


//    public void reduceQuantity(List<ItemQuantity> quantities) {
//
//        for (ItemQuantity quantity : quantities) {
//            Item item = itemRepository.findByItemUUID(quantity.getItemUUID()).orElseThrow(() -> new RuntimeException("아이템 없음"));
//            item.reduceQuantity(quantity.getQuantity());
//        }
//
//    }

//    public void addQuantity(List<ItemQuantity> quantities) {
//
//        for (ItemQuantity quantity : quantities) {
//            Item item = itemRepository.findByItemUUID(quantity.getItemUUID()).orElseThrow(() -> new RuntimeException("아이템 없음"));
//            item.addQuantity(quantity.getQuantity());
//        }
//    }
}
