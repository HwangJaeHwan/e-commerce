package com.example.reviewservice.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class PageResponse {

    private int totalPage;
    private long totalElement;
    private List<ReviewListResponse> items;
    private boolean isFirst;
    private boolean isLast;

    @Builder
    public PageResponse(int totalPage, long totalElement, List<ReviewListResponse> items,
                        boolean isFirst, boolean isLast) {
        this.totalPage = totalPage;
        this.totalElement = totalElement;
        this.items = items;
        this.isFirst = isFirst;
        this.isLast = isLast;
    }
}
