package com.meetix.meetix_api.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class EventRequest {

    @NotNull(message = "O tipo do evento é obrigatório.")
    private EventType eventType;

    @NotBlank(message = "O título é obrigatório.")
    private String title;

    private String description;

    @NotNull(message = "A data e hora de início são obrigatórias.")
    @Future(message = "A data de início deve ser no futuro.")
    private LocalDateTime startDateTime;

    @NotNull(message = "A data e hora de término são obrigatórias.")
    @Future(message = "A data de término deve ser no futuro.")
    private LocalDateTime endDateTime;

    private String location;
    private Integer maxAttendees;
    private Boolean isPaid;
    private BigDecimal price;
    private Long organizerId;
}
