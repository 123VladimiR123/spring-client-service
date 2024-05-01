package com.clientservice.springclientservice.configs;

import com.clientservice.springclientservice.repos.ProfileRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;

@Service
@RequiredArgsConstructor
public class CustomInterceptor implements HandlerInterceptor {

    @Value("${custom.gateway.address}")
    private String prefix;
    private final ProfileRepository repository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (repository.findByEmail(request.getHeader("email")) != null) {
            return true;
        }
        if (request.getRequestURI().equals("/info"))
            return true;

        response.sendRedirect(prefix + "/info");
        return false;
    }
}
