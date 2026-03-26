package com.connectcore.model.dto.notification;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class NotificationResponseDto {

    private Long id;
    private String message;
    private Boolean isRead;
    private LocalDateTime createdAt;
}