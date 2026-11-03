package com.example.order;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

/** Calls the user-service over HTTP. This is the service-to-service communication. */
@Component
public class UserClient {

    private static final Logger log = LoggerFactory.getLogger(UserClient.class);

    private final RestClient restClient;

    public UserClient(@Value("${user.service.url}") String baseUrl) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    public Optional<UserDto> findUser(long userId) {
        try {
            UserDto user = restClient.get()
                    .uri("/users/{id}", userId)
                    .retrieve()
                    .body(UserDto.class);
            return Optional.ofNullable(user);
        } catch (RestClientException ex) {
            // user-service is down or returned an error: don't crash the order-service
            log.warn("Could not fetch user {}: {}", userId, ex.getMessage());
            return Optional.empty();
        }
    }
}
