package com.connectcore.repository;

import com.connectcore.model.entity.Post;
import com.connectcore.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByUser(User user, Pageable pageable);
    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Query("""
    SELECT p FROM Post p
    JOIN FETCH p.user
    WHERE p.user IN :users
      AND p.isDeleted = false
    ORDER BY p.createdAt DESC
    """)
    Page<Post> findFeedForUsers(List<User> users, Pageable pageable);

    @Query("""
        SELECT p FROM Post p
        JOIN FETCH p.user
        WHERE p.id = :id
    """)
    Optional<Post> findByIdWithUser(Long id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM Post p WHERE p.id = :id")
    void hardDeleteById(Long id);
}