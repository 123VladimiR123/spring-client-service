package com.clientservice.springclientservice.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "messages")
public class MessageEntity {
    private String fromId;
    private String chatId;
    private String message;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime sent;
}
