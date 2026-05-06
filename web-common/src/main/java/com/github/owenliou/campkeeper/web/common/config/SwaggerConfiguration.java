package com.github.owenliou.campkeeper.web.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.nio.charset.StandardCharsets;

/**
 * 暫時不使用，用預設值
 */
@Configuration
public class SwaggerConfiguration {

    @Value("${swagger.description}")
    private String description;

    @Bean
    public OpenAPI customizeOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("MyData")
                        .description(new String(
                                description.getBytes(StandardCharsets.ISO_8859_1),
                                StandardCharsets.UTF_8
                        ))
                )
        ;
    }

}
