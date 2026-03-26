package com.connectcore.model.dto.follow;

import lombok.*;

@Getter
@AllArgsConstructor
@Builder
public class FollowResponseDto {

    private Long id;
    private Long followerId;
    private Long followingId;
}