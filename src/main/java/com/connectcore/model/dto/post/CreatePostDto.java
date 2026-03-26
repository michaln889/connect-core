package com.connectcore.model.dto.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePostDto {

    @NotBlank(message = "Content cannot be empty")
    @Size(max = 2000)
    private String content;
}