package com.github.owenliou.campkeeper.backend.app.config;

import com.google.genai.Client;
import com.google.genai.types.HttpOptions;
import org.springframework.ai.google.genai.GoogleGenAiEmbeddingConnectionDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GoogleGenAiEmbeddingConfig {

    @Bean
    public GoogleGenAiEmbeddingConnectionDetails googleGenAiEmbeddingConnectionDetails(
            @Value("${spring.ai.google.genai.embedding.api-key}") String apiKey) {

        Client client = Client.builder()
                .apiKey(apiKey)
                .httpOptions(HttpOptions.builder()
                        .apiVersion("v1")
                        .build())
                .build();

        return GoogleGenAiEmbeddingConnectionDetails.builder()
                .apiKey(apiKey)
                .genAiClient(client)
                .build();
    }
}
