package com.connectcore.model.mapper;

import com.connectcore.model.dto.message.MessageResponseDto;
import com.connectcore.model.entity.Message;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {

    public MessageResponseDto toDto(Message message) {
        return MessageResponseDto.builder()
                .id(message.getId())
                .content(message.getContent())
                .senderId(message.getSender().getId())
                .receiverId(message.getReceiver().getId())
                .createdAt(message.getCreatedAt())
                .isRead(message.getIsRead())
                .build();
    }
}