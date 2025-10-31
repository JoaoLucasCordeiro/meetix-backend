package com.meetix.meetix_api.domain.event;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record EventParticipantRequestDTO(
        @NotNull(message = "ID do evento é obrigatório")
        UUID eventId,

        @NotNull(message = "ID do usuário é obrigatório")
        UUID userId
) {}
