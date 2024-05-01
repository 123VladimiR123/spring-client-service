package com.clientservice.springclientservice.repos;

import com.clientservice.springclientservice.entities.MessageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;

public interface MessageRepository extends MongoRepository<MessageEntity, String> {

    Page<MessageEntity> findAllByChatId(String chatId, Pageable pageable);

    MessageEntity findByChatIdAndSent(String chatId, LocalDateTime sent);
}
