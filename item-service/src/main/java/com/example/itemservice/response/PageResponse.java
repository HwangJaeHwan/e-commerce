package com.example.itemservice.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
@Getter
public class PageResponse {
    private int totalPage;
    private long totalElement;
    private List<ItemResponse> items;
    private boolean isFirst;
    private boolean isLast;

    @Builder
    public PageResponse(int totalPage, long totalElement, List<ItemResponse> items,
                        boolean isFirst, boolean isLast) {
        this.totalPage = totalPage;
        this.totalElement = totalElement;
        this.items = items;
        this.isFirst = isFirst;
        this.isLast = isLast;
    }



}
