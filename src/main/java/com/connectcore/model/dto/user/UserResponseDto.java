package com.connectcore.model.dto.user;

import lombok.*;

@Getter
@AllArgsConstructor
@Builder
public class UserResponseDto {

    private Long id;
    private String username;
    private String email;
}