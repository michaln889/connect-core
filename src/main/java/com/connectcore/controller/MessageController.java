package com.connectcore.controller;

import com.connectcore.model.dto.message.MessageResponseDto;
import com.connectcore.model.dto.message.SendMessageDto;
import com.connectcore.model.entity.Message;
import com.connectcore.model.entity.User;
import com.connectcore.model.mapper.MessageMapper;
import com.connectcore.security.CustomUserDetails;
import com.connectcore.service.MessageService;
import com.connectcore.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final MessageMapper messageMapper;
    private final UserService userService;

    @PostMapping
    public MessageResponseDto sendMessage(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody @Valid SendMessageDto dto) {
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

    @GetMapping("/conversation")
    public List<MessageResponseDto> getConversation(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam Long otherUserId) {
        User currentUser = userDetails.getUser();

        return messageService.getConversation(currentUser.getId(), otherUserId)
                .stream()
                .map(messageMapper::toDto)
                .toList();
    }

    @PatchMapping("/{id}/read")
    public void markAsRead(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        messageService.markAsRead(id, user);
    }
}