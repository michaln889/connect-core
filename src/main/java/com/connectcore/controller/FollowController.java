package com.connectcore.controller;

import com.connectcore.model.entity.User;
import com.connectcore.security.CustomUserDetails;
import com.connectcore.service.FollowService;
import com.connectcore.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Follows", description = "Follow system")
@RestController
@RequestMapping("/api/follows")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;
    private final UserService userService;

    @Operation(summary = "Follow user")
    @Parameter(name = "followingId", description = "ID of the user to follow", example = "2")
    @ApiResponse(responseCode = "200", description = "Success")
    @ApiResponse(responseCode = "400", description = "Cannot follow yourself")
    @ApiResponse(responseCode = "409", description = "Already following user")
    @PostMapping("/{followingId}")
    public void follow(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long followingId) {
        User follower = userDetails.getUser();
        User following = userService.getById(followingId);

        followService.follow(follower, following);
    }

    @Operation(summary = "Unfollow user")
    @Parameter(name = "followingId", description = "ID of the user to unfollow", example = "2")
    @ApiResponse(responseCode = "200", description = "User unfollowed successfully")
    @DeleteMapping("/{followingId}")
    public void unfollow(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long followingId) {
        User follower = userDetails.getUser();
        User following = userService.getById(followingId);

        followService.unfollow(follower, following);
    }
}