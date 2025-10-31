package com.meetix.meetix_api.domain.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequestDTO(
        @Schema(example = "João", description = "Primeiro nome do usuário")
        @NotBlank(message = "Primeiro nome é obrigatório")
        String firstName,

        @Schema(example = "Silva", description = "Sobrenome do usuário")
        @NotBlank(message = "Sobrenome é obrigatório")
        String lastName,

        @Schema(example = "joao.silva@meetix.com", description = "Email do usuário")
        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        String email,

        @Schema(example = "senha123", description = "Senha do usuário (mínimo 6 caracteres)")
        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
        String password,

        @Schema(example = "@joaosilva", description = "Instagram do usuário (opcional)")
        String instagram,

        @Schema(example = "UFPE", description = "Universidade do usuário (opcional)")
        String university,

        @Schema(example = "Ciência da Computação", description = "Curso do usuário (opcional)")
        String course
) {
}
