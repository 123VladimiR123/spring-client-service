package com.clientservice.springclientservice.controllers;

import com.clientservice.springclientservice.dto.CommentDTO;
import com.clientservice.springclientservice.entities.CommentEntity;
import com.clientservice.springclientservice.entities.ProfileEntity;
import com.clientservice.springclientservice.mappers.CommentMapper;
import com.clientservice.springclientservice.repos.CommentsRepository;
import com.clientservice.springclientservice.repos.ProfileRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/profile/{id}")
public class ProfileController {

    @Value("${custom.gateway.address}")
    private String prefix;
    private final ProfileRepository profileRepository;
    private final CommentsRepository commentsRepository;
    private final CommentMapper commentMapper;

    @GetMapping
    public ModelAndView getProfilePage(HttpServletRequest request,
                                       @PathVariable("id") String id,
                                       @RequestParam(required = false, name = "q", defaultValue = "10") Integer page) {
        final ProfileEntity requestedProfile = profileRepository.findById(id).orElse(null);
        if (requestedProfile == null)
            return new ModelAndView("redirect:" + prefix + "/");

        final ProfileEntity currentProfile = profileRepository.findByEmail(request.getHeader("email"));

        Page<CommentEntity> commentEntityPage = commentsRepository.findAllByToProfileId(id,
                PageRequest.of(0, page, Sort.by("sent").descending()));

        Map<String, String> names = profileRepository.findAllById(commentEntityPage.getContent()
                .parallelStream()
                .map(CommentEntity::getOwnersId)
                .collect(Collectors.toSet()))
                .stream()
                .collect(Collectors.toMap(ProfileEntity::getId,
                        ProfileEntity::getFullName));

        names.put(id, requestedProfile.getFullName());

        ModelAndView modelAndView = new ModelAndView("profile");
        modelAndView.addObject("requested", requestedProfile);
        modelAndView.addObject("current", currentProfile);
        modelAndView.addObject("prefix", prefix);
        modelAndView.addObject("totalComments", commentEntityPage.getTotalElements());
        modelAndView.addObject("comments", commentEntityPage.getContent());
        modelAndView.addObject("query", page);
        modelAndView.addObject("queryplus", Integer.toString(page + 10));
        modelAndView.addObject("names", names);
        modelAndView.addObject("canComment", !(requestedProfile.getId().equals(currentProfile.getId()) ||
                commentsRepository.existsByToProfileIdAndOwnersId(id, currentProfile.getId())));
        modelAndView.addObject("self", requestedProfile.getId().equals(currentProfile.getId()));

        return modelAndView;
    }

    @PostMapping
    @ResponseBody
    @Transactional
    public void leaveComment(@PathVariable("id") String id,
                             @Valid @ModelAttribute CommentDTO commentDTO,
                             HttpServletRequest request,
                             HttpServletResponse response) throws IOException {

        ProfileEntity current = profileRepository.findByEmail(request.getHeader("email"));

        if (!commentsRepository.existsByToProfileIdAndOwnersId(id, current.getId())
                && !id.equals(current.getId())) {


            CommentEntity saved = commentsRepository.save(commentMapper.dtoToEntity(commentDTO, request, id));
            ProfileEntity requested = profileRepository.findById(id).get();
            int total = commentsRepository.countByToProfileId(requested.getId());

            requested.setRate((requested.getRate() *
                    (total - 1) + saved.getRate()) / (float) total);
            profileRepository.save(requested);
        }

        response.sendRedirect(prefix + "/" + request.getRequestURI());
    }

    @ExceptionHandler({NoSuchElementException.class})
    public void wrongUserExceptionHandler(HttpServletResponse response) throws IOException {
        response.sendRedirect("/");
    }
}
