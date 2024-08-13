package com.example.itemservice.request;

import com.example.itemservice.domain.item.Category;
import lombok.Data;

@Data
public class SearchRequest {

    private String search;

    private Category category;

}
