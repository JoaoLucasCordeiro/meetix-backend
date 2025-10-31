package com.meetix.meetix_api.domain.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdateDTO(
        @NotBlank(message = "Nome é obrigatório")
        String firstName,

        @NotBlank(message = "Sobrenome é obrigatório")
        String lastName,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        String email,

        @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
        String password,

        String instagram,

        String university,

        String course
) {}
