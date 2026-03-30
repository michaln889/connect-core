package com.connectcore.controller;

import com.connectcore.model.dto.notification.NotificationResponseDto;
import com.connectcore.model.entity.User;
import com.connectcore.security.CustomUserDetails;
import com.connectcore.service.NotificationService;
import com.connectcore.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Notifications", description = "User notifications")
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;

    @Operation(summary = "Get current user's notifications")
    @ApiResponse(responseCode = "200", description = "Notifications fetched successfully")
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

    @Operation(summary = "Mark notification as read")
    @Parameter(name = "id", description = "Notification ID", example = "1")
    @ApiResponse(responseCode = "200", description = "Notification marked as read")
    @ApiResponse(responseCode = "403", description = "Access denied")
    @ApiResponse(responseCode = "404", description = "Notification not found")
    @PatchMapping("/{id}/read")
    public void markAsRead(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        notificationService.markAsRead(id, user);
    }
}