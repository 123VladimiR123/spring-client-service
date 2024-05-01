package com.clientservice.springclientservice.repos;

import com.clientservice.springclientservice.entities.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentsRepository extends MongoRepository<CommentEntity, String> {
    int countByToProfileId(String profileId);
    Page<CommentEntity> findAllByToProfileId(String id, Pageable pageable);
    boolean existsByToProfileIdAndOwnersId(String toProfileId, String ownersId);
}
