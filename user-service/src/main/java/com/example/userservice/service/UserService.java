package com.example.userservice.service;

import com.example.userservice.domain.User;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.request.CreateUser;
import com.example.userservice.request.LoginRequest;
import com.example.userservice.request.PasswordChange;
import com.example.userservice.response.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.http.HttpRequest;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public void createUser(CreateUser createUser) {

        if (isLoginIdDuplicate(createUser.getLoginId())) {
            throw new RuntimeException("아이디 중복");
        }

        if (isEmailDuplicate(createUser)) {
            throw new RuntimeException("이메일 중복");
        }

        if (isPasswordMatch(createUser.getPassword(),createUser.getPasswordCheck())) {
            throw new RuntimeException("비밀번호 체크 오류");
        }

        userRepository.save(User.builder()
                .loginId(createUser.getLoginId())
                .password(passwordEncoder.encode(createUser.getPassword()))
                .email(createUser.getEmail())
                .userUUID(UUID.randomUUID().toString())
                .build());





    }

    public User login(LoginRequest loginRequest) {

        User loginUser = findUserByLoginId(loginRequest.getLoginId());
        validatePassword(loginRequest.getPassword(), loginUser.getPassword());

        return loginUser;
    }


    public UserInfoResponse info(Long id) {
        return new UserInfoResponse(userRepository.findById(id).orElseThrow(RuntimeException::new));
    }

    public void changePassword(Long userId, PasswordChange passwordChange) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("유저 없음"));
        validatePassword(passwordChange.getPassword(), user.getPassword());

        if (isPasswordMatch(passwordChange.getPassword(), passwordChange.getChangePasswordCheck())) {
            throw new RuntimeException("비밀번호 체크 오류");
        }
        
        user.changePassword(passwordEncoder.encode(passwordChange.getChangePassword()));



    }

    private User findUserByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId).orElseThrow(RuntimeException::new);
    }


    private void validatePassword(String inputPassword, String userPassword) {
        if (!passwordEncoder.matches(inputPassword, userPassword)) {
            throw new RuntimeException("비밀번호 다름");
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
}
