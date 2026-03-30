package com.connectcore.service;

import com.connectcore.exception.ConflictException;
import com.connectcore.exception.ResourceNotFoundException;
import com.connectcore.model.entity.User;
import com.connectcore.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldRegisterUser() {
        User user = User.builder()
                .username("test")
                .email("test@test.com")
                .build();

        when(userRepository.existsByUsername(any())).thenReturn(false);
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(user);
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");

        User result = userService.register(user);

        assertEquals("test", result.getUsername());
    }

    @Test
    void shouldThrowWhenUsernameExists() {
        User user = User.builder().username("test").build();

        when(userRepository.existsByUsername("test")).thenReturn(true);

        assertThrows(ConflictException.class, () -> userService.register(user));
    }

    @Test
    void shouldThrowWhenEmailExists() {
        User user = User.builder().email("test@test.com").build();

        when(userRepository.existsByUsername(any())).thenReturn(false);
        when(userRepository.existsByEmail(any())).thenReturn(true);

        assertThrows(ConflictException.class, () -> userService.register(user));
    }

    @Test
    void shouldReturnUserById() {
        User user = User.builder().id(1L).build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getById(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getById(1L));
    }
}