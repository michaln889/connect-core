package com.connectcore.service;

import com.connectcore.exception.AccessDeniedException;
import com.connectcore.exception.ConflictException;
import com.connectcore.exception.ResourceNotFoundException;
import com.connectcore.model.entity.User;
import com.connectcore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User register(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new ConflictException("Username already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ConflictException("Email already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public User updateUser(Long userId, String email, User currentUser) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!user.getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You can update only your profile");
        }

        user.setEmail(email);
        return userRepository.save(user);
    }

    @Transactional
    public void deactivateUser(Long userId, User currentUser) {
        if (!userId.equals(currentUser.getId())) {
            throw new AccessDeniedException("Access denied");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setIsActive(false);
        user.setIsDeleted(true);

        userRepository.save(user);
    }
}