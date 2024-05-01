package com.clientservice.springclientservice.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "chats")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatEntity {
    @Id
    private String id;
    private List<String> participationsId;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime lastMessage;
}
