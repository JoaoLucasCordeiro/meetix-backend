package com.meetix.meetix_api.domain.user;

public record UserUpdateDTO(
        String firstName,
        String lastName,
        String email,
        String password, // Opcional para update
        String instagram,
        String university,
        String course
) {
}