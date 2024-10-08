package com.example.itemservice.auth;

import com.example.itemservice.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@RequiredArgsConstructor
public class AuthResolver implements HandlerMethodArgumentResolver {


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserInfo.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

        log.info("uuid = {}", request.getHeader("uuid"));
        log.info("id = {}", request.getHeader("id"));
        String uuid = request.getHeader("uuid");
        String id = request.getHeader("id");

        if (uuid == null) {
            throw new UnauthorizedException();
        }

        return new UserInfo(Long.parseLong(id), uuid);
    }
}
