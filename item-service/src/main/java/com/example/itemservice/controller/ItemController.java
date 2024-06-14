package com.example.itemservice.controller;

import com.example.itemservice.request.ItemRequest;
import com.example.itemservice.response.ItemResponse;
import com.example.itemservice.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
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


}
