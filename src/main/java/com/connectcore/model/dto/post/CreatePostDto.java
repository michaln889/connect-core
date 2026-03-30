package com.connectcore.model.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Schema(description = "Request for creating a new post")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePostDto {

    @Schema(description = "Post content", example = "Hello world! This is my first post")
    @NotBlank(message = "Content cannot be empty")
    @Size(max = 2000)
    private String content;
}