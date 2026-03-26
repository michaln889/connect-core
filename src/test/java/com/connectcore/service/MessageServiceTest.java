package com.connectcore.service;

import com.connectcore.exception.AccessDeniedException;
import com.connectcore.model.entity.Message;
import com.connectcore.model.entity.User;
import com.connectcore.repository.MessageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;
    @InjectMocks
    private MessageService messageService;

    @Test
    void shouldSenMessage() {
        //given
        Message message = Message.builder().content("hi").build();
        when(messageRepository.save(message)).thenReturn(message);

        //when
        Message result = messageService.sendMessage(message);
        //then
        assertEquals("hi", result.getContent());
    }

    @Test
    void shouldMarkAsRead() {
        //given
        User user = User.builder().id(1L).build();
        Message message = Message.builder().id(1L).isRead(false).receiver(user).build();
        when(messageRepository.findById(1L)).thenReturn(Optional.of(message));
        //when
        messageService.markAsRead(1L, user);
        //then
        assertTrue(message.getIsRead());
    }

    @Test
    void shouldThrowWhenNotOwner() {
        //given
        User receiver = User.builder().id(1L).build();
        User other = User.builder().id(2L).build();
        Message msg = Message.builder().receiver(receiver).build();
        when(messageRepository.findById(1L)).thenReturn(Optional.of(msg));
        //when + then
        assertThrows(AccessDeniedException.class, () -> messageService.markAsRead(1L, other));
    }
}
