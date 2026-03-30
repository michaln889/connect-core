package com.connectcore.model.dto.message;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Schema(description = "Request for sending a message")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SendMessageDto {

    @Schema(description = "Message content", example = "Hey, how are you?")
    @NotBlank(message = "Message content cannot be empty")
    private String content;

    @Schema(description = "ID of the message receiver", example = "2")
    @NotNull(message = "Receiver is required")
    private Long receiverId;
}