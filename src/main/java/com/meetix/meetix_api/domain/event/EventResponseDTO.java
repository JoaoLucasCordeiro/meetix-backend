package com.meetix.meetix_api.domain.event;

import com.meetix.meetix_api.domain.user.UserResponseDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record EventResponseDTO(
        UUID id,
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
        OrganizerBasicDTO organizer,
        Boolean generateCertificate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static EventResponseDTO fromEntity(Event event) {
        return new EventResponseDTO(
                event.getId(),
                event.getEventType(),
                event.getTitle(),
                event.getDescription(),
                event.getStartDateTime(),
                event.getEndDateTime(),
                event.getLocation(),
                event.getImgUrl(),
                event.getEventUrl(),
                event.getRemote(),
                event.getMaxAttendees(),
                event.getIsPaid(),
                event.getPrice(),
                OrganizerBasicDTO.fromUser(event.getOrganizer()),
                event.getGenerateCertificate(),
                event.getCreatedAt(),
                event.getUpdatedAt()
        );
    }

    public record OrganizerBasicDTO(
            UUID id,
            String firstName,
            String lastName,
            String email,
            String instagram
    ) {
        public static OrganizerBasicDTO fromUser(com.meetix.meetix_api.domain.user.User user) {
            return new OrganizerBasicDTO(
                    user.getId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getInstagram()
            );
        }
    }
}
