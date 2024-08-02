package com.example.userservice.service;

import com.example.userservice.config.auth.UserInfo;
import com.example.userservice.domain.User;
import com.example.userservice.exception.*;
import com.example.userservice.messagequeue.KafkaProducer;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.request.CartRequest;
import com.example.userservice.request.CreateUser;
import com.example.userservice.request.LoginRequest;
import com.example.userservice.request.PasswordChange;
import com.example.userservice.response.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final KafkaProducer kafkaProducer;


    public void createUser(CreateUser createUser) {

        if (isLoginIdDuplicate(createUser.getLoginId())) {
            throw new DuplicateLoginIdException();
        }

        if (isEmailDuplicate(createUser)) {
            throw new DuplicateEmailException();
        }

        if (isPasswordMatch(createUser.getPassword(),createUser.getPasswordCheck())) {
            throw new PasswordCheckException();
        }

        userRepository.save(User.builder()
                .loginId(createUser.getLoginId())
                .password(passwordEncoder.encode(createUser.getPassword()))
                .email(createUser.getEmail())
                .userUUID(UUID.randomUUID().toString())
                .build());

    }

    public User login(LoginRequest loginRequest) {

        log.info("loginId : {}", loginRequest.getLoginId());
        log.info("password : {}", loginRequest.getPassword());

        User loginUser = findUserByLoginId(loginRequest.getLoginId());
        validatePassword(loginRequest.getPassword(), loginUser.getPassword());

        return loginUser;
    }


    public UserInfoResponse info(Long id) {
        return new UserInfoResponse(userRepository.findById(id).orElseThrow(UserNotFoundException::new));
    }

    public void changePassword(Long userId, PasswordChange passwordChange) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        validatePassword(passwordChange.getPassword(), user.getPassword());

        if (isPasswordMatch(passwordChange.getPassword(), passwordChange.getChangePasswordCheck())) {
            throw new PasswordCheckException();
        }

        user.changePassword(passwordEncoder.encode(passwordChange.getChangePassword()));

    }

    public Map<String, String> findLoginIds(Set<String> uuids) {
        log.info("in");
        log.info("size = {}", uuids.size());

        for (String uuid : uuids) {
            log.info("uuid = {}", uuid);
        }

        List<User> user = userRepository.findUserByUserUUIDIn(uuids);
        log.info("size = {}", user.size());
        log.info("info first: {}", user.getFirst());
        log.info("info last: {}", user.getLast());

        return user
                .stream().collect(Collectors.toMap(User::getUserUUID, User::getLoginId));

    }

    private User findUserByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId).orElseThrow(UserNotFoundException::new);
    }


    private void validatePassword(String inputPassword, String userPassword) {
        if (!passwordEncoder.matches(inputPassword, userPassword)) {
            throw new PasswordDiffException();
        }
    }

    private boolean isEmailDuplicate(CreateUser createUser) {
        return userRepository.findByEmail(createUser.getEmail()).isPresent();
    }

    private static boolean isPasswordMatch(String password, String passwordCheck) {
        return !password.equals(passwordCheck);
    }

    private boolean isLoginIdDuplicate(String loginId) {
        return userRepository.findByLoginId(loginId).isPresent();
    }

    public void produceCartMessage(UserInfo userInfo,CartRequest cartRequest) {

        log.info("userUUID = {}",userInfo.getUuid());

        cartRequest.setUserUUID(userInfo.getUuid());

        kafkaProducer.send("cart-topic", cartRequest);


    }


}
