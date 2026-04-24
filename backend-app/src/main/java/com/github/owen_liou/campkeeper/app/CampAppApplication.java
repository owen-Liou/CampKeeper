package com.github.owen_liou.campkeeper.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.github.owen_liou.campkeeper")
public class CampAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(CampAppApplication.class, args);
    }
}


