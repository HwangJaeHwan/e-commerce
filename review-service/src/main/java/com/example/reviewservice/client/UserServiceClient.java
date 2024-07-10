package com.example.reviewservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;
import java.util.Set;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    @PostMapping("/user-service/loginId")
    Map<String, String> findLoginIds(@RequestBody Set<String> uuids);


}
