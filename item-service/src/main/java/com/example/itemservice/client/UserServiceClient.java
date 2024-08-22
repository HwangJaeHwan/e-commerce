package com.example.itemservice.client;


import com.example.itemservice.data.UserType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    @GetMapping("/user-service/type/{userUUID}")
    UserType getType(@PathVariable String userUUID);
}
