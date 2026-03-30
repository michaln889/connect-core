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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Tag(name = "Posts", description = "Post management")
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostMapper postMapper;
    private final UserService userService;

    @Operation(summary = "Create new post")
    @ApiResponse(responseCode = "200", description = "Post created successfully")
    @ApiResponse(responseCode = "400", description = "Validation error")
    @PostMapping
    public PostResponseDto createPost(@AuthenticationPrincipal CustomUserDetails userDetails,
                                      @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Post data") @RequestBody @Valid CreatePostDto dto) {
        User user = userDetails.getUser();

        Post post = Post.builder()
                .content(dto.getContent())
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();

        return postMapper.toDto(postService.createPost(post));
    }

    @Operation(summary = "Get user feed")
    @GetMapping("/feed")
    public Page<PostResponseDto> getFeed(@AuthenticationPrincipal CustomUserDetails userDetails, @ParameterObject @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        User user = userDetails.getUser();

        return postService.getFeed(user, pageable)
                .map(postMapper::toDto);
    }

    @Operation(summary = "Get posts by user")
    @GetMapping("/user/{userId}")
    public Page<PostResponseDto> getUserPosts(@PathVariable Long userId, @ParameterObject @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        User user = userService.getById(userId);

        return postService.getUserPosts(user, pageable)
                .map(postMapper::toDto);
    }

    @Operation(summary = "Delete post")
    @Parameter(name = "postId", description = "ID of the post to delete", example = "1")
    @ApiResponse(responseCode = "200", description = "Post deleted successfully")
    @ApiResponse(responseCode = "403", description = "You are not the owner of this post")
    @ApiResponse(responseCode = "404", description = "Post not found")
    @DeleteMapping("/{postId}")
    public void deletePost(@PathVariable Long postId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        postService.deletePost(postId, user);
    }

    @Operation(summary = "Update post (only owner)")
    @Parameter(name = "postId", description = "ID of the post", example = "1")
    @Parameter(name = "userId", description = "ID of the user (owner)", example = "1")
    @ApiResponse(responseCode = "200", description = "Post updated successfully")
    @ApiResponse(responseCode = "400", description = "Validation error")
    @ApiResponse(responseCode = "403", description = "You are not the owner of this post")
    @ApiResponse(responseCode = "404", description = "Post not found")
    @PatchMapping("/{postId}")
    public PostResponseDto updatePost(@PathVariable Long postId, @AuthenticationPrincipal CustomUserDetails userDetails,
                                      @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated post data") @RequestBody UpdatePostDto dto) {
        User user = userDetails.getUser();
        Post post = postService.updatePost(postId, dto, user);
        return postMapper.toDto(post);
    }
}