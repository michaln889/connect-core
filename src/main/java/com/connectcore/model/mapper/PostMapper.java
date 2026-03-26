package com.connectcore.model.mapper;

import com.connectcore.model.dto.media.MediaDto;
import com.connectcore.model.dto.post.PostResponseDto;
import com.connectcore.model.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PostMapper {

    private final MediaMapper mediaMapper;

    public PostResponseDto toDto(Post post) {
        return PostResponseDto.builder()
                .id(post.getId())
                .content(post.getContent())
                .userId(post.getUser().getId())
                .username(post.getUser().getUsername())
                .createdAt(post.getCreatedAt())
                .media(mapMedia(post))
                .build();
    }

    public List<PostResponseDto> toDtoList(List<Post> posts) {
        return posts.stream()
                .map(this::toDto)
                .toList();
    }

    private List<MediaDto> mapMedia(Post post) {
        if (post.getMedia() == null) return List.of();

        return post.getMedia().stream()
                .filter(m -> !m.getIsDeleted())
                .map(mediaMapper::toDto)
                .toList();
    }
}