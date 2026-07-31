package com.user.service.app.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.user.service.app.dto.UserDto;
import com.user.service.app.entity.User;
import com.user.service.app.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserDto getUserById(Long id) {
        log.info("Fetching user by id: {}", id);
        return userRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<UserDto> listUsers() {
        return userRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

     @Transactional
    public UserDto createUser(String username, String email, String fullName) {
        log.info("Creating user: {}", username);
        User user = User.builder()
                .username(username)
                .email(email)
                .fullName(fullName)
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();
        return toDto(userRepository.save(user));
    }

    private UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .active(user.getActive())
                .build();
    }
}
