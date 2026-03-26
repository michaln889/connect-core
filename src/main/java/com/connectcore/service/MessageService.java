package com.connectcore.service;

import com.connectcore.exception.AccessDeniedException;
import com.connectcore.exception.ResourceNotFoundException;
import com.connectcore.model.entity.Message;
import com.connectcore.model.entity.User;
import com.connectcore.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    @Transactional
    public Message sendMessage(Message message) {
        return messageRepository.save(message);
    }

    public List<Message> getConversation(Long user1, Long user2) {
        return messageRepository.findConversation(user1, user2);
    }

    public List<Message> getUnreadMessages(User user) {
        return messageRepository.findByReceiverAndIsReadFalse(user);
    }

    @Transactional
    public void markAsRead(Long messageId, User currentUser) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found"));

        if (!message.getReceiver().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You cannot modify this message");
        }

        message.setIsRead(true);
        messageRepository.save(message);
    }

    @Transactional
    public void deleteMessageForUser(Long messageId, User currentUser) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found"));

        if (message.getSender().getId().equals(currentUser.getId())) {
            message.setDeletedBySender(true);
        } else if (message.getReceiver().getId().equals(currentUser.getId())) {
            message.setDeletedByReceiver(true);
        } else {
            throw new AccessDeniedException("Access denied");
        }

        messageRepository.save(message);
    }
}