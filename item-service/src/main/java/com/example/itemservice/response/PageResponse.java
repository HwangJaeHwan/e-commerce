package com.example.itemservice.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
@Getter
public class PageResponse {
    private int totalPage;
    private long totalElement;
    private List<ItemResponse> reviews;
    private boolean isFirst;
    private boolean isLast;

    @Builder
    public PageResponse(int totalPage, long totalElement, List<ItemResponse> reviews,
                        boolean isFirst, boolean isLast) {
        this.totalPage = totalPage;
        this.totalElement = totalElement;
        this.reviews = reviews;
        this.isFirst = isFirst;
        this.isLast = isLast;
    }

}
