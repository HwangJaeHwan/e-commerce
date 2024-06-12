package com.example.userservice.controller;

import com.example.userservice.config.auth.UserSession;
import com.example.userservice.domain.User;
import com.example.userservice.request.CreateUser;
import com.example.userservice.request.LoginRequest;
import com.example.userservice.response.UserInfoResponse;
import com.example.userservice.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class UserController {


    private final UserService userService;

    @PostMapping("/users")
    public void createUser(@RequestBody @Valid CreateUser createUser) {

        userService.createUser(createUser);

    }

    @PostMapping("/login")
    public void login(@RequestBody @Valid LoginRequest loginRequest, HttpServletRequest request) {

        User loginUser = userService.login(loginRequest);
        HttpSession session = request.getSession();
        session.setAttribute("login", loginUser.getId());
    }

    @GetMapping("/info")
    public UserInfoResponse info(UserSession userSession) {

        return userService.info(userSession.getId());
    }


}