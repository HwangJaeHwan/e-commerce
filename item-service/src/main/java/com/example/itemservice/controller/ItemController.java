package com.example.itemservice.controller;

import com.example.itemservice.auth.UserInfo;
import com.example.itemservice.request.ItemQuantity;
import com.example.itemservice.request.ItemRequest;
import com.example.itemservice.request.ItemUpdate;
import com.example.itemservice.request.SearchRequest;
import com.example.itemservice.response.CartItemResponse;
import com.example.itemservice.response.ItemDetailResponse;
import com.example.itemservice.response.PageResponse;
import com.example.itemservice.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/item-service")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;


    @GetMapping("/items")
    public PageResponse items(SearchRequest search, @RequestParam(defaultValue = "1") int page) {

        return itemService.items(search.getSearch(), search.getCategory(), page);

    }

    @PostMapping("/items/info")
    public List<CartItemResponse> cartItemsInfo(@RequestBody Map<String,Integer> infos) {

        return itemService.getCartItems(infos);

    }

    @GetMapping("/{itemUUID}")
    public ItemDetailResponse getItem(@PathVariable String itemUUID) {

        return itemService.getItem(itemUUID);

    }


    @PostMapping("/add")
    public Long addItem(@RequestBody @Valid ItemRequest itemRequest, UserInfo userInfo) {

        return itemService.addItem(itemRequest, userInfo);

    }

    @PostMapping("/amount")
    public BigDecimal amount(@RequestBody List<ItemQuantity> items) {

        return itemService.amount(items);

    }
    @DeleteMapping("/{itemId}")
    public void deleteItem(@PathVariable Long itemId,UserInfo userInfo) {

        itemService.deleteItem(itemId, userInfo);

    }

    @PatchMapping("/update/{itemID}")
    public void updateItem(@PathVariable Long itemID, @RequestBody ItemUpdate itemUpdate, UserInfo userInfo) {
        itemService.update(itemID, itemUpdate, userInfo);
    }





}
