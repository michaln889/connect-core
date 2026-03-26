package com.connectcore.model.dto.message;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class MessageResponseDto {

    private Long id;
    private String content;
    private Long senderId;
    private Long receiverId;
    private LocalDateTime createdAt;
    private Boolean isRead;
}