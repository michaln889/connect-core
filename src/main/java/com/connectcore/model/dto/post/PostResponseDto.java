package com.connectcore.model.dto.post;

import com.connectcore.model.dto.media.MediaDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Post response")
@Getter
@AllArgsConstructor
@Builder
public class PostResponseDto {

    @Schema(example = "1")
    private Long id;
    @Schema(example = "Hello world!")
    private String content;
    @Schema(example = "1")
    private Long userId;
    @Schema(example = "john123")
    private String username;
    @Schema(example = "2026-03-29T12:00:00")
    private LocalDateTime createdAt;
    @Schema(description = "List of media attached to the post")
    private List<MediaDto> media;
}