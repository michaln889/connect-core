package com.connectcore.model.dto.notification;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Schema(description = "Notification response")
@Getter
@AllArgsConstructor
@Builder
public class NotificationResponseDto {

    @Schema(example = "1")
    private Long id;
    @Schema(example = "User X started following you")
    private String message;
    @Schema(example = "false")
    private Boolean isRead;
    @Schema(example = "2026-03-29T12:00:00")
    private LocalDateTime createdAt;
}