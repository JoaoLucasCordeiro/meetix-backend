package com.meetix.meetix_api.domain.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @Schema(example = "joao@meetix.com", description = "Email do usuário")
        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        String email,

        @Schema(example = "senha123", description = "Senha do usuário")
        @NotBlank(message = "Senha é obrigatória")
        String password
) {
}
