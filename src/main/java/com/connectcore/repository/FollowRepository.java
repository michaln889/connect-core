package com.connectcore.repository;

import com.connectcore.model.entity.Follow;
import com.connectcore.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<Follow> findByFollower(User follower);   // kogo followuję
    List<Follow> findByFollowing(User following); // kto mnie followuje
    Optional<Follow> findByFollowerAndFollowing(User follower, User following);
    boolean existsByFollowerAndFollowing(User follower, User following);
    void deleteByFollowerAndFollowing(User follower, User following);

    @Query("""
        SELECT f.following FROM Follow f
        WHERE f.follower = :user
    """)
    List<User> findFollowedUsers(User user);
}