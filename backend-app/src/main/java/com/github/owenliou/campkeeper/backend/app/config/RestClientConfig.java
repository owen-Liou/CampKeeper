package com.github.owenliou.campkeeper.backend.app.config;

import com.github.owenliou.campkeeper.backend.app.external.icamping.variables.ICampingApiPath;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean("iCampingRestClient")
    public RestClient icampingRestClient() {
        return RestClient.builder()
                .baseUrl(ICampingApiPath.API_BASE_URL.getPath())
                .defaultHeader("Accept", "")
//                .defaultHeader("Origin", ICampingApiPath.CLIENT_HOST.getPath())
                .defaultHeader("Referer", ICampingApiPath.CLIENT_HOST.getPath())
                .build();
    }

}
