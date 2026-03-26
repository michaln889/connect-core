package com.connectcore.model.dto.media;

import lombok.*;

@Getter
@AllArgsConstructor
@Builder
public class MediaDto {

    private Long id;
    private String url;
    private String type;
}