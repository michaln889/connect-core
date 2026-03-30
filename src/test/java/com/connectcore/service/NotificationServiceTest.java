package com.connectcore.service;

import com.connectcore.exception.AccessDeniedException;
import com.connectcore.model.entity.Notification;
import com.connectcore.model.entity.User;
import com.connectcore.repository.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.control.MappingControl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;
    @InjectMocks
    private NotificationService notificationService;

    @Test
    void shouldMarkAsRead() {
        //given
        User user = User.builder().id(1L).build();
        Notification notification = Notification.builder().id(1L).user(user).isRead(false).build();
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));
        //when
        notificationService.markAsRead(1L, user);
        //then
        assertTrue(notification.getIsRead());
    }

    @Test
    void shouldThrowWhenNotOwner() {
        //given
        User owner = User.builder().id(1L).build();
        User other = User.builder().id(2L).build();
        Notification notification = Notification.builder().id(1L).user(owner).build();
        when(notificationRepository.findById(1L)).thenReturn(Optional.of(notification));
        //when + then
        assertThrows(AccessDeniedException.class, () -> notificationService.markAsRead(1L, other));
    }
}
