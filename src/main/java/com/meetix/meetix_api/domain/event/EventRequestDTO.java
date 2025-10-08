package com.meetix.meetix_api.domain.event;

import com.meetix.meetix_api.domain.enums.EventType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EventRequestDTO(
        EventType eventType,
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
        Boolean generateCertificate
) {
}