package com.clientservice.springclientservice.controllers;

import com.clientservice.springclientservice.entities.ProfileEntity;
import com.clientservice.springclientservice.repos.ProfileRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class Starter {

    @Value("${custom.gateway.address}")
    private String prefix;
    private final ProfileRepository profileRepository;

    @GetMapping("/")
    public void starter(HttpServletResponse response,
                          HttpServletRequest request) throws IOException {
        final ProfileEntity profile = profileRepository.findByEmail(request.getHeader("email"));

        response.sendRedirect(prefix + "/profile/" + profile.getId());
    }
}
