package com.connectcore.controller;

import com.connectcore.model.dto.post.CreatePostDto;
import com.connectcore.model.dto.post.PostResponseDto;
import com.connectcore.model.dto.post.UpdatePostDto;
import com.connectcore.model.entity.Post;
import com.connectcore.model.entity.User;
import com.connectcore.model.mapper.PostMapper;
import com.connectcore.security.CustomUserDetails;
import com.connectcore.service.PostService;
import com.connectcore.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostMapper postMapper;
    private final UserService userService;

    @PostMapping
    public PostResponseDto createPost(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody @Valid CreatePostDto dto) {
        User user = userDetails.getUser();

        Post post = Post.builder()
                .content(dto.getContent())
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();

        return postMapper.toDto(postService.createPost(post));
    }

    @GetMapping("/feed")
    public Page<PostResponseDto> getFeed(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        User user = userDetails.getUser();

        return postService.getFeed(user, pageable)
                .map(postMapper::toDto);
    }

    @GetMapping("/user/{userId}")
    public Page<PostResponseDto> getUserPosts(@PathVariable Long userId, @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        User user = userService.getById(userId);

        return postService.getUserPosts(user, pageable)
                .map(postMapper::toDto);
    }

    @DeleteMapping("/{postId}")
    public void deletePost(@PathVariable Long postId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        postService.deletePost(postId, user);
    }

    @PatchMapping("/{postId}")
    public PostResponseDto updatePost(@PathVariable Long postId, @RequestParam Long userId, @RequestBody UpdatePostDto dto) {
        User user = userService.getById(userId);
        Post post = postService.updatePost(postId, dto, user);
        return postMapper.toDto(post);
    }
}