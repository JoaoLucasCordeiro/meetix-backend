package com.meetix.meetix_api.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record EventAdminResponseDTO(
        UUID id,
        UUID userId,
        String userName,
        String userEmail,
        UUID invitedBy,
        String invitedByName,
        LocalDateTime invitedAt,
        Boolean accepted,
        LocalDateTime acceptedAt
) {
    public static EventAdminResponseDTO fromEntity(EventAdmin eventAdmin) {
        return new EventAdminResponseDTO(
                eventAdmin.getId(),
                eventAdmin.getUser().getId(),
                eventAdmin.getUser().getFirstName() + " " + eventAdmin.getUser().getLastName(),
                eventAdmin.getUser().getEmail(),
                eventAdmin.getInvitedBy().getId(),
                eventAdmin.getInvitedBy().getFirstName() + " " + eventAdmin.getInvitedBy().getLastName(),
                eventAdmin.getInvitedAt(),
                eventAdmin.getAccepted(),
                eventAdmin.getAcceptedAt()
        );
    }
}
