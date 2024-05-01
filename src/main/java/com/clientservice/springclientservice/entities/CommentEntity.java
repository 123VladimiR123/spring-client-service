package com.clientservice.springclientservice.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "comments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentEntity {

    @Id
    private String id;
    private String ownersId;
    private String toProfileId;
    private String message;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime sent;
    private short rate;
}
