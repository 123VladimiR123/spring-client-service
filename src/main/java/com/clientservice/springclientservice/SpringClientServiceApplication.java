package com.clientservice.springclientservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableDiscoveryClient
@SpringBootApplication
@EnableMongoRepositories
@EnableWebMvc
public class SpringClientServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringClientServiceApplication.class, args);
    }

}
