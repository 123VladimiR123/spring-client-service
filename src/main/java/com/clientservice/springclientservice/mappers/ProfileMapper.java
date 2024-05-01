package com.clientservice.springclientservice.mappers;

import com.clientservice.springclientservice.dto.ProfileDTO;
import com.clientservice.springclientservice.entities.ProfileEntity;
import com.clientservice.springclientservice.repos.ProfileRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class ProfileMapper {

    private final ProfileRepository profileRepository;

    @Transactional
    public ProfileEntity dtoToEntity(ProfileDTO dto, HttpServletRequest request) {
        return profileRepository.save(ProfileEntity.builder()
                .id("profile" + UUID.randomUUID())
                .email(request.getHeader("email"))
                .midName(convertToCurrentCase(dto.getMidName()))
                .surname(convertToCurrentCase(dto.getSurname()))
                .name(convertToCurrentCase(dto.getName()))
                .rate(0)
                .build());

    }

    private String convertToCurrentCase(String raw) {
        return raw.substring(0, 1).toUpperCase() + raw.substring(1).toLowerCase();
    }
}
