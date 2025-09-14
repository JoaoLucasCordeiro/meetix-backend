package com.meetix.meetix_api.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class EventResponse {
    private UUID id;
    private String eventType;
    private String title;
    private String description;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String location;
    private Integer maxAttendees;
    private Boolean isPaid;
    private BigDecimal price;
    private Long organizerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
