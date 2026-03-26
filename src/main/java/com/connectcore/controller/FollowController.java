package com.connectcore.controller;

import com.connectcore.model.entity.User;
import com.connectcore.security.CustomUserDetails;
import com.connectcore.service.FollowService;
import com.connectcore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/follows")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;
    private final UserService userService;

    @PostMapping
    public void follow(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam Long followingId) {
        User follower = userDetails.getUser();
        User following = userService.getById(followingId);

        followService.follow(follower, following);
    }

    @DeleteMapping
    public void unfollow(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam Long followingId) {
        User follower = userDetails.getUser();
        User following = userService.getById(followingId);

        followService.unfollow(follower, following);
    }
}