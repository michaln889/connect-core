package com.connectcore.model.dto.message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SendMessageDto {

    @NotBlank(message = "Message content cannot be empty")
    private String content;

    @NotNull(message = "Receiver is required")
    private Long receiverId;
}