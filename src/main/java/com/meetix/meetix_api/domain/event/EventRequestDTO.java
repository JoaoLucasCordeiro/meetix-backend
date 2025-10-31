package com.meetix.meetix_api.domain.event;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record EventRequestDTO(
        @Schema(example = "WORKSHOP", description = "Tipo do evento")
        @NotNull(message = "Tipo de evento é obrigatório")
        EventType eventType,

        @Schema(example = "Workshop de Spring Boot", description = "Título do evento")
        @NotBlank(message = "Título é obrigatório")
        @Size(max = 255, message = "Título não pode ter mais de 255 caracteres")
        String title,

        @Schema(example = "Aprenda Spring Boot na prática com projetos reais", description = "Descrição do evento")
        String description,

        @Schema(example = "2025-11-15T14:00:00", description = "Data e hora de início")
        @NotNull(message = "Data de início é obrigatória")
        @Future(message = "Data de início deve ser futura")
        LocalDateTime startDateTime,

        @Schema(example = "2025-11-15T18:00:00", description = "Data e hora de término")
        @NotNull(message = "Data de término é obrigatória")
        LocalDateTime endDateTime,

        @Schema(example = "Auditório Central - UFPE", description = "Local do evento (obrigatório se não for remoto)")
        String location,

        @Schema(example = "https://meetix.com/events/workshop-spring.jpg", description = "URL da imagem do evento")
        String imgUrl,

        @Schema(example = "https://meet.google.com/abc-defg-hij", description = "URL do evento online (obrigatório se for remoto)")
        String eventUrl,

        @Schema(example = "false", description = "Evento é remoto?")
        Boolean remote,

        @Schema(example = "50", description = "Número máximo de participantes")
        @Positive(message = "Número máximo de participantes deve ser positivo")
        Integer maxAttendees,

        @Schema(example = "false", description = "Evento é pago?")
        Boolean isPaid,

        @Schema(example = "50.00", description = "Preço do evento (obrigatório se for pago)")
        @DecimalMin(value = "0.0", message = "Preço deve ser maior ou igual a 0")
        BigDecimal price,

        @Schema(example = "550e8400-e29b-41d4-a716-446655440000", description = "ID do organizador (UUID)")
        @NotNull(message = "ID do organizador é obrigatório")
        UUID organizerId,

        @Schema(example = "true", description = "Gerar certificado de participação?")
        Boolean generateCertificate
) {
}
