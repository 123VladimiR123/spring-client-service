package com.clientservice.springclientservice.mappers;

import com.clientservice.springclientservice.dto.CommentDTO;
import com.clientservice.springclientservice.entities.CommentEntity;
import com.clientservice.springclientservice.repos.ProfileRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CommentMapper {

    private final ProfileRepository profileRepository;

    public CommentEntity dtoToEntity(CommentDTO dto, HttpServletRequest request, String toProfileId) {
        return CommentEntity.builder()
                .ownersId(profileRepository.findByEmail(request.getHeader("email")).getId())
                .message(dto.getMessage())
                .rate(dto.getRate())
                .sent(LocalDateTime.now())
                .id("comment-" + UUID.randomUUID())
                .toProfileId(toProfileId)
                .build();
    }
}
