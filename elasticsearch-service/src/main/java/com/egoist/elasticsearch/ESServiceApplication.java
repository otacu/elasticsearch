package com.egoist.elasticsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class ESServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ESServiceApplication.class, args);
    }
}
