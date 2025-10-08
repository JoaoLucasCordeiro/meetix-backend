package com.meetix.meetix_api.domain.event;

import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EventRequestDTO(
        String eventType,
        String title,
        String description,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime,
        String location,
        String imgUrl,
        String eventUrl,
        Boolean remote,
        Integer maxAttendees,
        Boolean isPaid,
        BigDecimal price,
        Long organizerId,
        MultipartFile image
) {
}