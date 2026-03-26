package com.connectcore.service;

import com.connectcore.exception.BadRequestException;
import com.connectcore.exception.ConflictException;
import com.connectcore.model.entity.Follow;
import com.connectcore.model.entity.User;
import com.connectcore.repository.FollowRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class FollowServiceTest {

    @Mock
    private FollowRepository followRepository;

    @InjectMocks
    private FollowService followService;

    @Test
    void shouldFollowUser() {
        User user1 = User.builder().id(1L).build();
        User user2 = User.builder().id(2L).build();

        when(followRepository.existsByFollowerAndFollowing(user1, user2)).thenReturn(false);

        followService.follow(user1, user2);

        verify(followRepository).save(any(Follow.class));
    }

    @Test
    void shouldThrowWhenFollowYourself() {
        User user = User.builder().id(1L).build();
        assertThrows(BadRequestException.class, () -> followService.follow(user, user));
    }

    @Test
    void shouldThrowWhenAlreadyFollowing() {
        User user1 = User.builder().id(1L).build();
        User user2 = User.builder().id(2L).build();

        when(followRepository.existsByFollowerAndFollowing(user1, user2)).thenReturn(true);

        assertThrows(ConflictException.class, () -> followService.follow(user1, user2));
    }
}
