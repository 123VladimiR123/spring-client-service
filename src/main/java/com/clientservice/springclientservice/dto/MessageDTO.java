package com.clientservice.springclientservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class MessageDTO {
    @Size(max = 255, min = 1)
    @NotNull
    private final String message;
}
