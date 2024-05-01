package com.clientservice.springclientservice.mappers;

import com.clientservice.springclientservice.dto.MessageDTO;
import com.clientservice.springclientservice.entities.MessageEntity;
import com.clientservice.springclientservice.repos.ChatRepository;
import com.clientservice.springclientservice.repos.ProfileRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class MessageMapper {

    private final ProfileRepository profileRepository;
    private final ChatRepository chatRepository;

    @Transactional
    public MessageEntity dtoToEntity(MessageDTO messageDTO, HttpServletRequest request, String chatId)
            throws AccessDeniedException {

        String id = profileRepository.findByEmail(request.getHeader("email")).getId();

        if (!chatRepository.findById(chatId).orElse(null)
                .getParticipationsId().contains(id))
            throw new AccessDeniedException("You can't send messages to this chat");

        return MessageEntity.builder()
                .message(messageDTO.getMessage())
                .fromId(id)
                .sent(LocalDateTime.now())
                .chatId(chatId)
                .build();
    }
}
