package com.meetix.meetix_api.domain.event;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EventAdminInviteRequestDTO(
        @Schema(example = "maria@meetix.com", description = "Email do usuário a ser convidado")
        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        String email
) {
}
