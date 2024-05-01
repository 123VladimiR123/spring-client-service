package com.clientservice.springclientservice.controllers;

import com.clientservice.springclientservice.dto.ProfileDTO;
import com.clientservice.springclientservice.entities.ProfileEntity;
import com.clientservice.springclientservice.mappers.ProfileMapper;
import com.clientservice.springclientservice.repos.ProfileRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/info")
public class InfoController {

    @Value("${custom.gateway.address}")
    private String prefix;
    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;

    @GetMapping
    public ModelAndView getInfoPage(@RequestParam(name = "error", required = false) String error,
                                    HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("info");
        final ProfileEntity profile = profileRepository.findByEmail(request.getHeader("email"));

        if ("true".equals(error)) {
            modelAndView.addObject("error", true);
        }

        if (profile != null) {
            modelAndView.addObject("profile", profile);
        }

        return modelAndView;
    }

    @ResponseBody
    @PostMapping
    public void postInfoPage(HttpServletRequest request,
                             HttpServletResponse response,
                             @ModelAttribute("profile") @Valid ProfileDTO profileDTO) throws IOException {

        if (!profileRepository.existsByEmail(request.getHeader("email"))) {
            profileMapper.dtoToEntity(profileDTO, request);
        }

        response.sendRedirect(prefix + "/");
    }

    @ExceptionHandler({ValidationException.class})
    @ResponseBody
    public void infoValidateExceptionHandler(HttpServletResponse response) throws IOException {
        response.sendRedirect(prefix + "/info?error");
    }
}
