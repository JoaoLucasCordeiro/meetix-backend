package com.meetix.meetix_api.domain.event;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EventUpdateDTO(
        @Schema(example = "WORKSHOP", description = "Tipo do evento (opcional)")
        EventType eventType,

        @Schema(example = "Workshop de Spring Boot - ATUALIZADO", description = "Título do evento (opcional)")
        @Size(max = 200, message = "Título deve ter no máximo 200 caracteres")
        String title,

        @Schema(example = "Descrição atualizada", description = "Descrição do evento (opcional)")
        @Size(max = 2000, message = "Descrição deve ter no máximo 2000 caracteres")
        String description,

        @Schema(example = "2025-12-15T14:00:00", description = "Data de início (opcional)")
        LocalDateTime startDateTime,

        @Schema(example = "2025-12-15T18:00:00", description = "Data de término (opcional)")
        LocalDateTime endDateTime,

        @Schema(example = "Auditório Principal", description = "Localização (opcional)")
        String location,

        @Schema(example = "https://meetix.com/event-updated.jpg", description = "URL da imagem (opcional)")
        String imgUrl,

        @Schema(example = "https://meet.google.com/abc", description = "URL do evento online (opcional)")
        String eventUrl,

        @Schema(example = "false", description = "Evento é remoto? (opcional)")
        Boolean remote,

        @Schema(example = "100", description = "Número máximo de participantes (opcional)")
        @Min(value = 1, message = "Número máximo de participantes deve ser no mínimo 1")
        Integer maxAttendees,

        @Schema(example = "true", description = "Evento é pago? (opcional)")
        Boolean isPaid,

        @Schema(example = "50.00", description = "Preço do evento (opcional)")
        @DecimalMin(value = "0.0", inclusive = false, message = "Preço deve ser maior que zero")
        BigDecimal price,

        @Schema(example = "true", description = "Gerar certificado? (opcional)")
        Boolean generateCertificate
) {}
