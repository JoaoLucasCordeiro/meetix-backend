package com.meetix.meetix_api.domain.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record EventParticipantResponseDTO(
        UUID id,
        UUID eventId,
        String eventTitle,
        ParticipantBasicDTO participant,
        LocalDateTime registrationDate,
        Boolean attended,
        LocalDateTime checkedInAt
) {
    public static EventParticipantResponseDTO fromEntity(EventParticipant eventParticipant) {
        return new EventParticipantResponseDTO(
                eventParticipant.getId(),
                eventParticipant.getEvent().getId(),
                eventParticipant.getEvent().getTitle(),
                ParticipantBasicDTO.fromUser(eventParticipant.getUser()),
                eventParticipant.getRegistrationDate(),
                eventParticipant.getAttended(),
                eventParticipant.getCheckedInAt()
        );
    }

    public record ParticipantBasicDTO(
            UUID id,
            String firstName,
            String lastName,
            String email,
            String instagram,
            String university
    ) {
        public static ParticipantBasicDTO fromUser(com.meetix.meetix_api.domain.user.User user) {
            return new ParticipantBasicDTO(
                    user.getId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getInstagram(),
                    user.getUniversity()
            );
        }
    }
}
