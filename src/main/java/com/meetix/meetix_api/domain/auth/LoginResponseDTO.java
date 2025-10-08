package com.meetix.meetix_api.domain.auth;

import java.util.UUID;

public record LoginResponseDTO(
        String token,
        UUID userId,
        String email,
        String firstName,
        String lastName
) {
}