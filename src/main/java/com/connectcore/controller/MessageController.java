package com.connectcore.controller;

import com.connectcore.model.dto.message.MessageResponseDto;
import com.connectcore.model.dto.message.SendMessageDto;
import com.connectcore.model.entity.Message;
import com.connectcore.model.entity.User;
import com.connectcore.model.mapper.MessageMapper;
import com.connectcore.security.CustomUserDetails;
import com.connectcore.service.MessageService;
import com.connectcore.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Messages", description = "Messaging system")
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final MessageMapper messageMapper;
    private final UserService userService;

    @Operation(summary = "Send message")
    @ApiResponse(responseCode = "200", description = "Message sent successfully")
    @ApiResponse(responseCode = "400", description = "Validation error")
    @ApiResponse(responseCode = "404", description = "Receiver not found")
    @PostMapping
    public MessageResponseDto sendMessage(@AuthenticationPrincipal CustomUserDetails userDetails,
                                          @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Message data") @RequestBody @Valid SendMessageDto dto) {
        User sender = userDetails.getUser();
        User receiver = userService.getById(dto.getReceiverId());

        Message message = Message.builder()
                .content(dto.getContent())
                .sender(sender)
                .receiver(receiver)
                .createdAt(LocalDateTime.now())
                .isRead(false)
                .build();

        return messageMapper.toDto(messageService.sendMessage(message));
    }

    @Operation(summary = "Get conversation with another user")
    @Parameter(name = "otherUserId", description = "ID of the other user", example = "2")
    @ApiResponse(responseCode = "200", description = "Conversation fetched successfully")
    @GetMapping("/conversation")
    public List<MessageResponseDto> getConversation(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam Long otherUserId) {
        User currentUser = userDetails.getUser();

        return messageService.getConversation(currentUser.getId(), otherUserId)
                .stream()
                .map(messageMapper::toDto)
                .toList();
    }

    @Operation(summary = "Mark message as read")
    @ApiResponse(responseCode = "200", description = "Message marked as read")
    @ApiResponse(responseCode = "403", description = "You cannot modify this message")
    @ApiResponse(responseCode = "404", description = "Message not found")
    @PatchMapping("/{id}/read")
    public void markAsRead(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        messageService.markAsRead(id, user);
    }
}