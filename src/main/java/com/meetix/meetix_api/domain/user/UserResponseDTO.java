package com.meetix.meetix_api.domain.user;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String instagram,
        String university,
        String course,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static UserResponseDTO fromEntity(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getInstagram(),
                user.getUniversity(),
                user.getCourse(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
