package com.example.reviewservice.data.order;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class OrderResponse {

    private Long orderId;
    private String orderUUID;
    private String userUUID;
    private OrderStatus orderStatus;

    private List<ItemResponse> items;





}
