package com.connectcore.repository;

import com.connectcore.model.entity.Media;
import com.connectcore.model.entity.Post;
import com.connectcore.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MediaRepository extends JpaRepository<Media, Long> {

    List<Media> findByPost(Post post);

    List<Media> findByUser(User user);
}
