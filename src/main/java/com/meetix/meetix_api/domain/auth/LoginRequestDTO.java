package com.meetix.meetix_api.domain.auth;

public record LoginRequestDTO(
        String email,
        String password
) {
}