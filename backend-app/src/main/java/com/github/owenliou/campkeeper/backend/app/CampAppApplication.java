package com.github.owenliou.campkeeper.backend.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan(basePackages = "com.github.owenliou.campkeeper.model.entity")
@SpringBootApplication(scanBasePackages = "com.github.owenliou.campkeeper")
public class CampAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(CampAppApplication.class, args);
    }
}


