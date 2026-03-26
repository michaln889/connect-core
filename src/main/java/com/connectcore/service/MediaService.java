package com.connectcore.service;

import com.connectcore.exception.AccessDeniedException;
import com.connectcore.exception.ResourceNotFoundException;
import com.connectcore.model.entity.Media;
import com.connectcore.model.entity.Post;
import com.connectcore.model.entity.User;
import com.connectcore.repository.MediaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MediaService {

    private final MediaRepository mediaRepository;

    @Transactional
    public Media save(Media media) {
        return mediaRepository.save(media);
    }

    public List<Media> getPostMedia(Post post) {
        return mediaRepository.findByPost(post);
    }

    public List<Media> getUserMedia(User user) {
        return mediaRepository.findByUser(user);
    }

    @Transactional
    public void deleteMedia(Long mediaId, User currentUser) {
        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new ResourceNotFoundException("Media not found"));

        boolean isOwner =
                (media.getUser() != null && media.getUser().getId().equals(currentUser.getId())) ||
                        (media.getPost() != null && media.getPost().getUser().getId().equals(currentUser.getId()));

        if (!isOwner) {
            throw new AccessDeniedException("Access denied");
        }

        mediaRepository.delete(media);
    }

    @Transactional
    public void softDeleteMedia(Long mediaId, User currentUser) {
        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new AccessDeniedException("Media not found"));

        boolean isOwner =
                (media.getUser() != null && media.getUser().getId().equals(currentUser.getId())) ||
                        (media.getPost() != null && media.getPost().getUser().getId().equals(currentUser.getId()));

        if (!isOwner) {
            throw new AccessDeniedException("Access denied");
        }

        media.setIsDeleted(true);
        mediaRepository.save(media);
    }
}