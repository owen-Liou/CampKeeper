package com.github.owenliou.campkeeper.base.service;

import org.springframework.stereotype.Service;

@Service
public class BaseGreetingService {

    public String buildGreeting(String moduleName) {
        return "CampKeeper ready from " + moduleName;
    }
}

