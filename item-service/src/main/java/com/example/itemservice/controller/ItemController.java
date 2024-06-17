package com.example.itemservice.controller;

import com.example.itemservice.request.ItemQuantity;
import com.example.itemservice.request.ItemRequest;
import com.example.itemservice.response.ItemResponse;
import com.example.itemservice.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/item-service")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;


    @GetMapping("/")
    public List<ItemResponse> items() {

         return itemService.items();

    }

    @PostMapping("/add")
    public void addItem(@RequestBody @Valid ItemRequest itemRequest) {

        itemService.addItem(itemRequest);

    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@PathVariable String itemId) {

        itemService.deleteItem(itemId);
    }

    @PatchMapping("/quantity/add")
    public void quantityAdd(@RequestBody List<ItemQuantity> quantities) {
        itemService.addQuantity(quantities);

    }

    @PatchMapping("/quantity/reduce")
    public void quantityReduce(@RequestBody List<ItemQuantity> quantities) {
        itemService.reduceQuantity(quantities);

    }


}
