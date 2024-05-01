package com.clientservice.springclientservice.repos;

import com.clientservice.springclientservice.entities.ProfileEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProfileRepository extends MongoRepository<ProfileEntity, String> {

    ProfileEntity findByEmail(String email);
    boolean existsByEmail(String email);
}
