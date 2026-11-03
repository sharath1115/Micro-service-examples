package com.example.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class UserServiceTest {

    private final UserService service = new UserService();

    @Test
    void findById_whenUserExists_returnsUser() {
        assertEquals("Asha", service.findById(1).name());
    }

    @Test
    void findById_whenUserNotFound_throwsNotFoundException() {
        assertThrows(UserNotFoundException.class, () -> service.findById(999));
    }
}
