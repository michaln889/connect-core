package com.connectcore.model.mapper;

import com.connectcore.model.dto.media.MediaDto;
import com.connectcore.model.entity.Media;
import org.springframework.stereotype.Component;

@Component
public class MediaMapper {

    public MediaDto toDto(Media media) {
        return MediaDto.builder()
                .id(media.getId())
                .url(media.getUrl())
                .type(media.getType().name())
                .build();
    }
}