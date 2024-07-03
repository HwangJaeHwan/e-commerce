package com.example.payservice.client;

import com.example.payservice.request.ItemQuantity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.List;

@FeignClient(name = "item-service")
public interface ItemServiceClient {

    @PostMapping("/item-service/amount")
    BigDecimal amount(@RequestBody List<ItemQuantity> items);
}
