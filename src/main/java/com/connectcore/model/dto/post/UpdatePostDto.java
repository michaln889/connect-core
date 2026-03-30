package com.connectcore.model.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Schema(description = "Request for updating an existing post")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePostDto {

    @Schema(description = "Updated post content", example = "Updated content of the post")
    @NotBlank(message = "Content cannot be empty")
    @Size(max = 2000)
    private String content;
}
