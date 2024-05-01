package com.clientservice.springclientservice.entities;

import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "profiles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileEntity {
    @Id
    private String id;
    private String surname;
    private String name;
    private String midName;
    private String email;
    private float rate;

    @Transient
    public String getFullName() {
        return surname + " " + name + " " + midName;
    }
}
