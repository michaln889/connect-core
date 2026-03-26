package com.connectcore.model.dto.post;

import com.connectcore.model.dto.media.MediaDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class PostResponseDto {

    private Long id;
    private String content;
    private Long userId;
    private String username;
    private LocalDateTime createdAt;
    private List<MediaDto> media;
}