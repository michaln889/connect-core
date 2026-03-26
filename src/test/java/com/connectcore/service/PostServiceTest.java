package com.connectcore.service;

import com.connectcore.exception.AccessDeniedException;
import com.connectcore.model.dto.post.UpdatePostDto;
import com.connectcore.model.entity.Post;
import com.connectcore.model.entity.User;
import com.connectcore.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.control.MappingControl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @InjectMocks
    private FollowService followService;

    @Test
    void shouldCreatePost() {
        //given
        Post post = Post.builder().content("test").build();
        when(postRepository.save(post)).thenReturn(post);
        //when
        Post result = postService.createPost(post);

        //then
        assertEquals("test", result.getContent());
    }

    @Test
    void shouldUpdatePost_whenOwner() {
        //given
        User user = User.builder().id(1L).build();
        Post post = Post.builder().id(1L).user(user).content("old").build();

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(postRepository.save(any())).thenReturn(post);

        UpdatePostDto dto = new UpdatePostDto("new");
        //when
        Post updated = postService.updatePost(post.getId(), dto, user);

        //then
        assertEquals("new", updated.getContent());
    }

    @Test
    void shouldThrowWhenUpdatingNotOwner() {
        //given
        User owner = User.builder().id(1L).build();
        User other = User.builder().id(2L).build();
        Post post = Post.builder().id(1L).user(owner).build();

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        //when + then
        assertThrows(AccessDeniedException.class, () -> postService.updatePost(1L, new UpdatePostDto("x"), other));
    }

    @Test
    void shouldDeletePost_whenOwner() {
        //given
        User user = User.builder().id(1L).build();
        Post post = Post.builder().id(1L).user(user).build();

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        //when
        postService.deletePost(1L, user);

        //then
        verify(postRepository).delete(post);
    }
}
