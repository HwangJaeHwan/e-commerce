package com.example.userservice.controller;

import com.example.userservice.config.auth.UserSession;
import com.example.userservice.config.jwt.JwtProvider;
import com.example.userservice.domain.User;
import com.example.userservice.request.CreateUser;
import com.example.userservice.request.LoginRequest;
import com.example.userservice.request.PasswordChange;
import com.example.userservice.response.UserInfoResponse;
import com.example.userservice.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.ws.rs.core.HttpHeaders;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static jakarta.ws.rs.core.HttpHeaders.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class UserController {


    private final UserService userService;
    private final JwtProvider jwtProvider;

    @PostMapping("/users")
    public void createUser(@RequestBody @Valid CreateUser createUser) {

        userService.createUser(createUser);

    }

    @PostMapping("/login")
    public void login(@RequestBody @Valid LoginRequest loginRequest, HttpServletResponse response) {

        User loginUser = userService.login(loginRequest);
        String token = jwtProvider.createToken(loginUser.getId(), loginUser.getUserUUID());


        response.addHeader(AUTHORIZATION, "Bearer " + token);
    }

    @GetMapping("/info")
    public UserInfoResponse info(UserSession userSession) {

        return userService.info(userSession.getId());
    }

    @PatchMapping("/passwordChange")
    public void passwordChange(UserSession userSession, @RequestBody PasswordChange passwordChange) {

        userService.changePassword(userSession.getId(),passwordChange);
    }


}