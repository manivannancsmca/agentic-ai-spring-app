package com.user.service.app.util;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.user.service.app.service.UserService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;

    @Override
    public void run(String... args) {

        if (userService.listUsers().isEmpty()) {
            userService.createUser("john_doe", "john@example.com", "John Doe");
            userService.createUser("jane_smith", "jane@example.com", "Jane Smith");
            userService.createUser("bob_wilson", "bob@example.com", "Bob Wilson");
        }
    }
}
