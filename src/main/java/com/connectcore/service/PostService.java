package com.connectcore.service;

import com.connectcore.exception.AccessDeniedException;
import com.connectcore.exception.ResourceNotFoundException;
import com.connectcore.model.dto.post.UpdatePostDto;
import com.connectcore.model.entity.Post;
import com.connectcore.model.entity.User;
import com.connectcore.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.Update;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final FollowService followService;

    @Transactional
    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    public Page<Post> getUserPosts(User user, Pageable pageable) {
        return postRepository.findByUser(user, pageable);
    }

    public Page<Post> getFeed(User user, Pageable pageable) {
        List<User> followedUsers = followService.getFollowedUsers(user);
        followedUsers.add(user);

        return postRepository.findFeedForUsers(followedUsers, pageable);
    }

    public Page<Post> getGlobalFeed(Pageable pageable) {
        return postRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    @Transactional
    public Post updatePost(Long postId, UpdatePostDto dto, User currentUser) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        if (!post.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You are not the owner of this post");
        }

        post.setContent(dto.getContent());
        return postRepository.save(post);
    }

    @Transactional
    public void deletePost(Long postId, User currentUser) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        if (!post.getUser().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("You are not the owner of this post");
        }

        postRepository.delete(post);
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    @Transactional
    public void hardDeletePost(Long id) {
        postRepository.hardDeleteById(id);
    }
}