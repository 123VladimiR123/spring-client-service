package com.clientservice.springclientservice.repos;

import com.clientservice.springclientservice.entities.ChatEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatRepository extends MongoRepository<ChatEntity, String> {

    ChatEntity findByParticipationsId(List<String> list);
    Page<ChatEntity> findAllByParticipationsIdContainingAndLastMessageNotEmpty(String id, Pageable pageable);
}
