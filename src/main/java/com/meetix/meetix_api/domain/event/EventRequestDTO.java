package com.meetix.meetix_api.domain.event;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record EventRequestDTO(
        @NotNull(message = "Tipo do evento é obrigatório")
        EventType eventType,

        @NotBlank(message = "Título é obrigatório")
        @Size(max = 200, message = "Título deve ter no máximo 200 caracteres")
        String title,

        @Size(max = 2000, message = "Descrição deve ter no máximo 2000 caracteres")
        String description,

        @NotNull(message = "Data e hora de início são obrigatórias")
        @Future(message = "Data de início deve ser no futuro")
        LocalDateTime startDateTime,

        @NotNull(message = "Data e hora de término são obrigatórias")
        LocalDateTime endDateTime,

        String location,

        String imgUrl,

        String eventUrl,

        Boolean remote,

        @Min(value = 1, message = "Número máximo de participantes deve ser no mínimo 1")
        Integer maxAttendees,

        Boolean isPaid,

        @DecimalMin(value = "0.0", inclusive = false, message = "Preço deve ser maior que zero")
        BigDecimal price,

        @NotNull(message = "ID do organizador é obrigatório")
        UUID organizerId,

        Boolean generateCertificate
) {}
