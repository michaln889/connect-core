package com.connectcore.controller;

import com.connectcore.model.dto.user.UserRegisterDto;
import com.connectcore.model.dto.user.UserResponseDto;
import com.connectcore.model.entity.User;
import com.connectcore.model.enums.UserRole;
import com.connectcore.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Tag(name = "Users", description = "User management endpoints")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Register new user")
    @ApiResponse(responseCode = "200", description = "User registered successfully")
    @ApiResponse(responseCode = "400", description = "Validation error")
    @ApiResponse(responseCode = "409", description = "Username or email already exists")
    @PostMapping("/register")
    public UserResponseDto register(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User registration data") @RequestBody @Valid UserRegisterDto dto) {
        User user = User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .role(UserRole.USER)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();

        User saved = userService.register(user);

        return UserResponseDto.builder()
                .id(saved.getId())
                .username(saved.getUsername())
                .email(saved.getEmail())
                .build();
    }

    @Operation(summary = "Get user by ID")
    @ApiResponse(responseCode = "200", description = "User fetched successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @GetMapping("/{id}")
    public UserResponseDto getUser(@PathVariable Long id) {
        User user = userService.getById(id);

        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }
}