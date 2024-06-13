package com.example.itemservice.controller;

import com.example.itemservice.request.ItemRequest;
import com.example.itemservice.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;


    @PostMapping("/add")
    public void addItem(@RequestBody @Valid ItemRequest itemRequest) {

        itemService.addItem(itemRequest);

    }


}
