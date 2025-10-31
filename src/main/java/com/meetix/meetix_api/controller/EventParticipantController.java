package com.meetix.meetix_api.controller;

import com.meetix.meetix_api.domain.event.EventParticipantRequestDTO;
import com.meetix.meetix_api.domain.event.EventParticipantResponseDTO;
import com.meetix.meetix_api.service.EventParticipantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/event-participants")
@RequiredArgsConstructor
@Tag(name = "Participação em Eventos", description = "Gerenciamento de participações em eventos")
@SecurityRequirement(name = "bearerAuth")
public class EventParticipantController {

    private final EventParticipantService participantService;

    @PostMapping("/register")
    @Operation(summary = "Inscrever em evento", description = "Registra um usuário em um evento")
    public ResponseEntity<EventParticipantResponseDTO> registerParticipant(
            @Valid @RequestBody EventParticipantRequestDTO data
    ) {
        EventParticipantResponseDTO participant = participantService.registerParticipant(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(participant);
    }

    @DeleteMapping("/cancel")
    @Operation(summary = "Cancelar inscrição", description = "Cancela a participação de um usuário em um evento")
    public ResponseEntity<Void> cancelRegistration(
            @RequestParam UUID eventId,
            @RequestParam UUID userId
    ) {
        participantService.cancelRegistration(eventId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/event/{eventId}")
    @Operation(summary = "Listar participantes de evento", description = "Retorna todos os participantes de um evento")
    public ResponseEntity<List<EventParticipantResponseDTO>> getEventParticipants(
            @PathVariable UUID eventId
    ) {
        List<EventParticipantResponseDTO> participants = participantService.getEventParticipants(eventId);
        return ResponseEntity.ok(participants);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Listar eventos do usuário", description = "Retorna todos os eventos que um usuário está participando")
    public ResponseEntity<List<EventParticipantResponseDTO>> getUserEvents(
            @PathVariable UUID userId
    ) {
        List<EventParticipantResponseDTO> events = participantService.getUserEvents(userId);
        return ResponseEntity.ok(events);
    }

    @PostMapping("/check-in")
    @Operation(summary = "Fazer check-in", description = "Registra presença de um participante no evento")
    public ResponseEntity<EventParticipantResponseDTO> checkIn(
            @RequestParam UUID eventId,
            @RequestParam UUID userId
    ) {
        EventParticipantResponseDTO participant = participantService.checkIn(eventId, userId);
        return ResponseEntity.ok(participant);
    }

    @GetMapping("/is-registered")
    @Operation(summary = "Verificar inscrição", description = "Verifica se um usuário está inscrito em um evento")
    public ResponseEntity<Map<String, Boolean>> isUserRegistered(
            @RequestParam UUID eventId,
            @RequestParam UUID userId
    ) {
        boolean isRegistered = participantService.isUserRegistered(eventId, userId);
        return ResponseEntity.ok(Map.of("isRegistered", isRegistered));
    }

    @GetMapping("/count/{eventId}")
    @Operation(summary = "Contar participantes", description = "Retorna número de participantes de um evento")
    public ResponseEntity<Map<String, Long>> getParticipantCount(
            @PathVariable UUID eventId
    ) {
        long count = participantService.getParticipantCount(eventId);
        return ResponseEntity.ok(Map.of("count", count));
    }

    @GetMapping("/attended/{eventId}")
    @Operation(summary = "Listar presenças confirmadas", description = "Retorna participantes que fizeram check-in")
    public ResponseEntity<List<EventParticipantResponseDTO>> getAttendedParticipants(
            @PathVariable UUID eventId
    ) {
        List<EventParticipantResponseDTO> participants = participantService.getAttendedParticipants(eventId);
        return ResponseEntity.ok(participants);
    }
}
