package com.connectcore.controller;

import com.connectcore.model.dto.notification.NotificationResponseDto;
import com.connectcore.model.entity.User;
import com.connectcore.security.CustomUserDetails;
import com.connectcore.service.NotificationService;
import com.connectcore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;

    @GetMapping
    public List<NotificationResponseDto> getUserNotifications(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();

        return notificationService.getUserNotifications(user)
                .stream()
                .map(n -> NotificationResponseDto.builder()
                        .id(n.getId())
                        .message(n.getMessage())
                        .isRead(n.getIsRead())
                        .createdAt(n.getCreatedAt())
                        .build())
                .toList();
    }

    @PatchMapping("/{id}/read")
    public void markAsRead(@PathVariable Long id, @RequestParam Long userId) {
        User user = userService.getById(userId);
        notificationService.markAsRead(id, user);
    }
}