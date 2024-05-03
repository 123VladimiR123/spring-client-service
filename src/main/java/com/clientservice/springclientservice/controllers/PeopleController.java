package com.clientservice.springclientservice.controllers;

import com.clientservice.springclientservice.entities.ProfileEntity;
import com.clientservice.springclientservice.repos.ProfileRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class PeopleController {

    @Value("${custom.gateway.address}")
    private String prefix;
    private final ProfileRepository profileRepository;

    @GetMapping
    public ModelAndView getPeople(@RequestParam(name = "q", required = false, defaultValue = "10") Integer query,
                                  HttpServletRequest request) {
        Page<ProfileEntity> entities = profileRepository.findAll(
                PageRequest.of(0, query, Sort.by("rate").descending()));

        ProfileEntity current = profileRepository.findByEmail(request.getHeader("email"));

        ModelAndView modelAndView = new ModelAndView("people");
        modelAndView.addObject("peoplePage", entities);
        modelAndView.addObject("current", current);
        modelAndView.addObject("prefix", prefix);
        modelAndView.addObject("query", query);
        modelAndView.addObject("queryplus", Integer.toString(query + 10));

        return modelAndView;
    }
}
