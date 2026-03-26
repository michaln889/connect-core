package com.connectcore.service;

import com.connectcore.exception.AccessDeniedException;
import com.connectcore.model.entity.Media;
import com.connectcore.model.entity.User;
import com.connectcore.repository.MediaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class MediaServiceTest {

    @Mock
    private MediaRepository mediaRepository;
    @InjectMocks
    private MediaService mediaService;

    @Test
    void shouldSoftDeleteMedia() {
        //given
        User user = User.builder().id(1L).build();
        Media media = Media.builder().id(1L).isDeleted(false).user(user).build();
        when(mediaRepository.findById(1L)).thenReturn(Optional.of(media));
        //when
        mediaService.softDeleteMedia(1L, user);
        //then
        assertTrue(media.getIsDeleted());
    }

    @Test
    void shouldThrowWhenNotOwner() {
        //given
        User owner = User.builder().id(1L).build();
        User other = User.builder().id(2L).build();
        Media media = Media.builder().id(1L).isDeleted(false).user(owner).build();
        when(mediaRepository.findById(1L)).thenReturn(Optional.of(media));
        //when + then
        assertThrows(AccessDeniedException.class, () -> mediaService.softDeleteMedia(1L, other));
    }
}
