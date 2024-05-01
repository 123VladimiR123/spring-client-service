package com.clientservice.springclientservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class CommentDTO {
    @Max(value = 5)
    @Min(value = 1)
    private final short rate;

    @Size(max = 255, min = 15)
    private final String message;
}
