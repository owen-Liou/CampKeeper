package com.github.owenliou.campkeeper.base.utils.web;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.DefaultRedirectStrategy;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpClientUtils {

//    public static final MediaType TEXT_HTML_UTF8 = new MediaType(MediaType.TEXT_HTML, StandardCharsets.UTF_8);
//    public static final MediaType TEXT_PLAIN_UTF8 = new MediaType(MediaType.TEXT_PLAIN, StandardCharsets.UTF_8);

    private static final WebClient webClient = WebClient.builder().build();
    private static RestTemplate restTemplate;

    public static String get(String url) {
        URI uri = UriComponentsBuilder.fromUriString(url).build().encode().toUri();
        return getRestTemplate().getForObject(uri, String.class);
    }

    public static String post(String url, MediaType mediaType, Object data) {
        URI uri = UriComponentsBuilder.fromUriString(url).build().encode().toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        ResponseEntity<String> response = getRestTemplate().exchange(uri, HttpMethod.POST, new HttpEntity<>(data, headers), String.class);
        return response.getBody();
    }

    public static Mono<String> rxGet(String url) {
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                ;
    }

    public static Mono<String> rxPost(String url, MediaType mediaType, Object data) {
        return webClient.post()
                .uri(url) // 設定 URL
                .contentType(mediaType) // 設定 Content-Type
                .bodyValue(data) // 設定請求體
                .retrieve() // 發送請求並獲取響應
                .bodyToMono(String.class) // 將響應轉換為 Mono<String>
//                .doOnError(e -> log.error("Error during POST request: {}", e.getMessage()))
                ;
    }

    public static String put(String url, MediaType mediaType, Object data) {
        URI uri = UriComponentsBuilder.fromUriString(url).build().encode().toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        ResponseEntity<String> response = getRestTemplate().exchange(uri, HttpMethod.PUT, new HttpEntity<>(data, headers), String.class);
        return response.getBody();
    }

    public static Mono<String> rxPut(String url, MediaType mediaType, Object data) {
        return webClient.put()
                .uri(url) // 設定 URL
                .contentType(mediaType) // 設定 Content-Type
                .bodyValue(data) // 設定請求體
                .retrieve() // 發送請求並獲取響應
                .bodyToMono(String.class) // 將響應轉換為 Mono<String>
//                .doOnError(e -> log.error("Error during POST request: {}", e.getMessage()))
                ;
    }

    private static RestTemplate getRestTemplate() {
        if (restTemplate == null) {
            HttpClient httpClient = HttpClients.custom().setRedirectStrategy(new DefaultRedirectStrategy()).build();
            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
//            restTemplate = new RestTemplateBuilder().requestFactory(() -> requestFactory).build();
            restTemplate = new RestTemplate(requestFactory);
        }
        return restTemplate;
    }

}
