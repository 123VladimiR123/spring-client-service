package com.clientservice.springclientservice.controllers;

import com.clientservice.springclientservice.dto.MessageDTO;
import com.clientservice.springclientservice.entities.ChatEntity;
import com.clientservice.springclientservice.entities.MessageEntity;
import com.clientservice.springclientservice.entities.ProfileEntity;
import com.clientservice.springclientservice.mappers.MessageMapper;
import com.clientservice.springclientservice.repos.ChatRepository;
import com.clientservice.springclientservice.repos.MessageRepository;
import com.clientservice.springclientservice.repos.ProfileRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    @Value("${custom.gateway.address}")
    private String prefix;
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final ProfileRepository profileRepository;
    private final MessageMapper messageMapper;

    @GetMapping("/to-{id}")
    @ResponseBody
    public void chatToUser(HttpServletResponse response,
                           HttpServletRequest request,
                           @PathVariable("id") String id) throws IOException {

        ProfileEntity current = profileRepository.findByEmail(request.getHeader("email"));
        ProfileEntity requested = profileRepository.findById(id).get();


        ArrayList<String> list = new ArrayList(2);
        list.add(current.getId());
        list.add(requested.getId());
        Collections.sort(list);

        ChatEntity chat = chatRepository.findByParticipationsId(list);
        if (chat == null) {
            chat = chatRepository.save(ChatEntity.builder()
                    .id("chat-" + UUID.randomUUID())
                    .participationsId(list)
                    .lastMessage(null)
                    .build());
        }

        response.sendRedirect(prefix + "/chat/" + chat.getId());
    }

    @PostMapping("/{chatId}")
    @ResponseBody
    public void sendMessage(HttpServletResponse response,
                            HttpServletRequest request,
                            @PathVariable("chatId") String id,
                            @Valid @ModelAttribute("message") MessageDTO dto) throws IOException {
        ChatEntity chat = chatRepository.findById(id).get();
        ProfileEntity profile = profileRepository.findByEmail(request.getHeader("email"));

        if (!chat.getParticipationsId().contains(profile.getId()))
            throw new AccessDeniedException("You are not in this chat");

        MessageEntity msg = messageMapper.dtoToEntity(dto, request, id);

        chat.setLastMessage(msg.getSent());

        chatRepository.save(chat);
        messageRepository.save(msg);

        response.sendRedirect(prefix + "/chat/" + id);
    }

    @GetMapping("/{chatId}")
    public ModelAndView getChat(@PathVariable("chatId") String chatId,
                                @RequestParam(value = "q", required = false, defaultValue = "20") Integer query,
                                HttpServletRequest request) throws AccessDeniedException {

        ChatEntity chat = chatRepository.findById(chatId).get();
        ProfileEntity current =  profileRepository.findByEmail(request.getHeader("email"));

        if (!chat.getParticipationsId().contains(current.getId()))
            throw new AccessDeniedException("You are not in this chat");

        ModelAndView modelAndView = new ModelAndView("chat");
        Page<MessageEntity> messageEntityPage = messageRepository.findAllByChatId(chatId,
                PageRequest.of(0, query, Sort.by("sent").ascending()));


        modelAndView.addObject("current", current);
        modelAndView.addObject("requested", profileRepository.findById(chat.getParticipationsId().stream()
                .filter(e -> !e.equals(current.getId())).findFirst().get()).get());
        modelAndView.addObject("page", messageEntityPage);
        modelAndView.addObject("query", query);
        modelAndView.addObject("prefix", prefix);
        modelAndView.addObject("chatId", chatId);

        return modelAndView;
    }

    @GetMapping
    public ModelAndView getChats(HttpServletRequest request,
                                 HttpServletResponse response,
                                 @RequestParam(value = "q", required = false, defaultValue = "20") Integer query) {
        ProfileEntity current = profileRepository.findByEmail(request.getHeader("email"));

        ModelAndView allChats = new ModelAndView("allChats");
        Page<ChatEntity> chatEntities = chatRepository.findAllByParticipationsIdContainsAndLastMessageNotNull(current.getId(),
                PageRequest.of(0, query, Sort.by("lastMessage").descending()));

        Map<String, ProfileEntity> map = chatEntities.getContent().stream().collect(Collectors.toMap(e -> e.getId(),
                e -> profileRepository.findById(e.getParticipationsId().stream().filter(f -> !f.equals(current.getId())).findFirst().get()).get()));

        allChats.addObject("query", query);
        allChats.addObject("current", current);
        allChats.addObject("page", chatEntities);
        allChats.addObject("names", map);
        allChats.addObject("prefix", prefix);

        return allChats;
    }

    @ExceptionHandler({NoSuchElementException.class, ValidationException.class, AccessDeniedException.class})
    @ResponseBody
    public void wrongUser(HttpServletResponse response) throws IOException {
        response.sendRedirect(prefix + "/");
    }
}
