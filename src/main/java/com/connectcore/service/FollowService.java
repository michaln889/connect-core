package com.connectcore.service;

import com.connectcore.exception.BadRequestException;
import com.connectcore.exception.ConflictException;
import com.connectcore.model.entity.Follow;
import com.connectcore.model.entity.User;
import com.connectcore.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;

    @Transactional
    public void follow(User follower, User following) {
        if (follower.equals(following)) {
            throw new BadRequestException("You cannot follow yourself");
        }

        if (followRepository.existsByFollowerAndFollowing(follower, following)) {
            throw new ConflictException("Already following this user");
        }

        Follow follow = Follow.builder()
                .follower(follower)
                .following(following)
                .build();

        followRepository.save(follow);
    }

    @Transactional
    public void unfollow(User follower, User following) {
        followRepository.deleteByFollowerAndFollowing(follower, following);
    }

    public List<User> getFollowedUsers(User user) {
        return followRepository.findFollowedUsers(user);
    }

    public List<Follow> getFollowers(User user) {
        return followRepository.findByFollowing(user);
    }
}