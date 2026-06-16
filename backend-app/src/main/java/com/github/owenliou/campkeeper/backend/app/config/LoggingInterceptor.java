package com.github.owenliou.campkeeper.backend.app.config;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StopWatch;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * REST API 請求/回應日誌攔截器
 */
@Slf4j
public class LoggingInterceptor implements ClientHttpRequestInterceptor {

    @NotNull
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte [] body, ClientHttpRequestExecution execution) throws IOException {
        long timestamp = System.currentTimeMillis();
        StopWatch stopWatch = new StopWatch();

        try {
            stopWatch.start();
            logRequest(timestamp, request, body);
            ClientHttpResponse response = execution.execute(request, body);
            stopWatch.stop();
            logResponse(timestamp, response, stopWatch.getTotalTimeMillis());
            return response;
        } catch (IOException e) {
            logError(timestamp, e);
            throw e;
        }
    }

    private void logRequest(long timestamp, HttpRequest request, byte[] body) {
        log.info("""
                        
                    ╔══ {} BEGIN ══╗
                    │ URI    : {}
                    │ Method : {}
                    │ Headers: {}
                    │ Body   : {}
                    """,
                timestamp,
                request.getURI(),
                request.getMethod(),
                request.getHeaders(),
                new String(body, StandardCharsets.UTF_8));
    }

    private void logResponse(long timestamp, ClientHttpResponse response, long duration) throws IOException {
        String status = response.getStatusCode().toString();
        log.info("""
                    │ Status : {} ({}ms)
                    │ Length : {}
                    │ Headers: {}
                    ╚══ {} END ══╝
                    """,
                status,
                duration,
                response.getHeaders().getContentLength(),
                response.getHeaders(),
                timestamp);
    }

    private void logError(long timestamp, IOException e) {
        log.error("╚══ {} ERROR: {} ══╝", timestamp, e.getLocalizedMessage(), e);
    }
}