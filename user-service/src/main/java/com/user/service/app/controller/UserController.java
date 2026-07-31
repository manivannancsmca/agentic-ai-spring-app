package com.user.service.app.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.service.app.dto.CreateUserRequest;
import com.user.service.app.dto.UserDto;
import com.user.service.app.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> listUsers() {
        return ResponseEntity.ok(userService.listUsers());
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody CreateUserRequest request) {
        return ResponseEntity.ok(userService.createUser(
                request.getUsername(), request.getEmail(), request.getFullName()));
    }
}
