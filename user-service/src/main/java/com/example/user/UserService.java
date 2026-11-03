package com.example.user;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    // In-memory "database" - each microservice owns its own data
    private final List<User> users = List.of(
            new User(1, "Asha", "asha@example.com"),
            new User(2, "Ravi", "ravi@example.com"));

    public List<User> findAll() {
        return users;
    }

    public User findById(long id) {
        log.info("Looking up user {}", id);
        return users.stream()
                .filter(u -> u.id() == id)
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
