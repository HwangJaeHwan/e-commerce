package com.example.userservice.controller;

import com.example.userservice.config.auth.UserSession;
import com.example.userservice.config.jwt.JwtProvider;
import com.example.userservice.domain.User;
import com.example.userservice.request.CartRequest;
import com.example.userservice.request.CreateUser;
import com.example.userservice.request.LoginRequest;
import com.example.userservice.request.PasswordChange;
import com.example.userservice.response.Token;
import com.example.userservice.response.UserInfoResponse;
import com.example.userservice.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

import static jakarta.ws.rs.core.HttpHeaders.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/user-service")
public class UserController {


    private final UserService userService;
    private final JwtProvider jwtProvider;

    @PostMapping("/users")
    public void createUser(@RequestBody @Valid CreateUser createUser) {

        userService.createUser(createUser);

    }

    @PostMapping("/login")
    public Token login(@RequestBody @Valid LoginRequest loginRequest, HttpServletResponse response) {

        User loginUser = userService.login(loginRequest);
        String token = jwtProvider.createToken(loginUser.getId(), loginUser.getUserUUID());


        return new Token("Bearer " + token);
    }

    @GetMapping("/info")
    public UserInfoResponse info(UserSession userSession) {

        return userService.info(userSession.getId());
    }

    @PostMapping("/loginId")
    public Map<String, String> findLoginIds(@RequestBody Set<String> uuids) {

        return userService.findLoginIds(uuids);
    }

    @PatchMapping("/passwordChange")
    public void passwordChange(UserSession userSession, @RequestBody PasswordChange passwordChange) {

        userService.changePassword(userSession.getId(), passwordChange);
    }

    @PostMapping("/cart/message")
    public void cartAdd(UserSession userSession, @RequestBody CartRequest cartRequest) {

        cartRequest.setUserUUID(userSession.getUuid());
//        userService.produceCartMessage(cartRequest);
    }

    @GetMapping("/cart/items")
    public void cartItems(UserSession userSession) {
//        orderServiceClient.getCartItems(userSession.getUuid());
//        itemServiceClient.getItemInfo()
    }
}