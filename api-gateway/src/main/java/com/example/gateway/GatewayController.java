package com.example.gateway;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

/** Single entry point: clients only talk to the gateway, never directly to a service. */
@RestController
@RequestMapping("/api")
public class GatewayController {

    private static final Logger log = LoggerFactory.getLogger(GatewayController.class);

    private final RestClient userClient;
    private final RestClient orderClient;

    public GatewayController(@Value("${user.service.url}") String userUrl,
                             @Value("${order.service.url}") String orderUrl) {
        this.userClient = RestClient.builder().baseUrl(userUrl).build();
        this.orderClient = RestClient.builder().baseUrl(orderUrl).build();
    }

    @GetMapping("/users")
    public ResponseEntity<?> users() {
        return forward(userClient, "/users");
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> user(@PathVariable long id) {
        return forward(userClient, "/users/" + id);
    }

    @GetMapping("/orders")
    public ResponseEntity<?> orders() {
        return forward(orderClient, "/orders");
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<?> order(@PathVariable long id) {
        return forward(orderClient, "/orders/" + id);
    }

    private ResponseEntity<?> forward(RestClient client, String path) {
        try {
            // exchange() lets us pass through 404s etc. instead of throwing
            return client.get().uri(path).exchange((req, res) -> ResponseEntity
                    .status(res.getStatusCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(res.bodyTo(String.class)));
        } catch (RestClientException ex) {
            log.error("Downstream call to {} failed: {}", path, ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body(Map.of("error", "Service unavailable", "path", path));
        }
    }
}
