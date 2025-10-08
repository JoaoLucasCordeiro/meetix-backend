package com.meetix.meetix_api.domain.user;

public record UserRequestDTO(
        String firstName,
        String lastName,
        String email,
        String password,
        String instagram,
        String university,
        String course
) {
}
