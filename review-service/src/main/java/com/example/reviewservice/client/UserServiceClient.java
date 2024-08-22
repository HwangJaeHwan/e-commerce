package com.example.reviewservice.client;

import com.example.reviewservice.data.UserType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;
import java.util.Set;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    @PostMapping("/user-service/loginId")
    Map<String, String> findLoginIds(@RequestBody Set<String> uuids);

    @GetMapping("/user-service/type/{userUUID}")
    UserType getType(@PathVariable String userUUID);

}
