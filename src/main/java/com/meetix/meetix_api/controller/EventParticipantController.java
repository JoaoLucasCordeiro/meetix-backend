package com.meetix.meetix_api.controller;

import com.meetix.meetix_api.domain.event.EventParticipantRequestDTO;
import com.meetix.meetix_api.domain.event.EventParticipantResponseDTO;
import com.meetix.meetix_api.service.EventParticipantService;
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
public class EventParticipantController {

    private final EventParticipantService participantService;

    @PostMapping("/register")
    public ResponseEntity<EventParticipantResponseDTO> registerParticipant(
            @Valid @RequestBody EventParticipantRequestDTO data
    ) {
        EventParticipantResponseDTO participant = participantService.registerParticipant(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(participant);
    }

    @DeleteMapping("/cancel")
    public ResponseEntity<Void> cancelRegistration(
            @RequestParam UUID eventId,
            @RequestParam UUID userId
    ) {
        participantService.cancelRegistration(eventId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<EventParticipantResponseDTO>> getEventParticipants(
            @PathVariable UUID eventId
    ) {
        List<EventParticipantResponseDTO> participants = participantService.getEventParticipants(eventId);
        return ResponseEntity.ok(participants);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<EventParticipantResponseDTO>> getUserEvents(
            @PathVariable UUID userId
    ) {
        List<EventParticipantResponseDTO> events = participantService.getUserEvents(userId);
        return ResponseEntity.ok(events);
    }

    @PostMapping("/check-in")
    public ResponseEntity<EventParticipantResponseDTO> checkIn(
            @RequestParam UUID eventId,
            @RequestParam UUID userId
    ) {
        EventParticipantResponseDTO participant = participantService.checkIn(eventId, userId);
        return ResponseEntity.ok(participant);
    }

    @GetMapping("/is-registered")
    public ResponseEntity<Map<String, Boolean>> isUserRegistered(
            @RequestParam UUID eventId,
            @RequestParam UUID userId
    ) {
        boolean isRegistered = participantService.isUserRegistered(eventId, userId);
        return ResponseEntity.ok(Map.of("isRegistered", isRegistered));
    }

    @GetMapping("/count/{eventId}")
    public ResponseEntity<Map<String, Long>> getParticipantCount(
            @PathVariable UUID eventId
    ) {
        long count = participantService.getParticipantCount(eventId);
        return ResponseEntity.ok(Map.of("count", count));
    }

    @GetMapping("/attended/{eventId}")
    public ResponseEntity<List<EventParticipantResponseDTO>> getAttendedParticipants(
            @PathVariable UUID eventId
    ) {
        List<EventParticipantResponseDTO> participants = participantService.getAttendedParticipants(eventId);
        return ResponseEntity.ok(participants);
    }
}
