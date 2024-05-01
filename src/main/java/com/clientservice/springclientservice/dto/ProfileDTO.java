package com.clientservice.springclientservice.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class ProfileDTO {
    @Size(min = 2, max = 30)
    @Pattern(regexp = "^[а-яА-Я]*$")
    private final String surname;
    @Size(min = 2, max = 30)
    @Pattern(regexp = "^[а-яА-Я]*$")
    private final String name;
    @Size(min = 2, max = 30)
    @Pattern(regexp = "^[а-яА-Я]*$")
    private final String midName;
}
