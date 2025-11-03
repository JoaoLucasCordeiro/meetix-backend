package com.meetix.meetix_api.controller;

import com.meetix.meetix_api.domain.event.EventAdminInviteRequestDTO;
import com.meetix.meetix_api.domain.event.EventAdminResponseDTO;
import com.meetix.meetix_api.service.EventAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/events/{eventId}/admins")
@RequiredArgsConstructor
@Tag(name = "Event Admins", description = "Gerenciamento de administradores de eventos")
@SecurityRequirement(name = "bearerAuth")
public class EventAdminController {

    private final EventAdminService eventAdminService;

    @PostMapping("/invite")
    @Operation(summary = "Convidar usuário para ser admin do evento")
    public ResponseEntity<EventAdminResponseDTO> inviteAdmin(
            @PathVariable UUID eventId,
            @Valid @RequestBody EventAdminInviteRequestDTO request,
            Authentication authentication
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        EventAdminResponseDTO response = eventAdminService.inviteAdmin(eventId, request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/accept")
    @Operation(summary = "Aceitar convite para ser admin")
    public ResponseEntity<EventAdminResponseDTO> acceptInvite(
            @PathVariable UUID eventId,
            Authentication authentication
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        EventAdminResponseDTO response = eventAdminService.acceptInvite(eventId, userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/decline")
    @Operation(summary = "Recusar convite para ser admin")
    public ResponseEntity<Void> declineInvite(
            @PathVariable UUID eventId,
            Authentication authentication
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        eventAdminService.declineInvite(eventId, userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{adminId}")
    @Operation(summary = "Remover admin do evento (apenas criador)")
    public ResponseEntity<Void> removeAdmin(
            @PathVariable UUID eventId,
            @PathVariable UUID adminId,
            Authentication authentication
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        eventAdminService.removeAdmin(eventId, adminId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Listar admins do evento")
    public ResponseEntity<List<EventAdminResponseDTO>> listEventAdmins(@PathVariable UUID eventId) {
        List<EventAdminResponseDTO> admins = eventAdminService.listEventAdmins(eventId);
        return ResponseEntity.ok(admins);
    }

    @GetMapping("/pending")
    @Operation(summary = "Listar convites pendentes do usuário autenticado")
    public ResponseEntity<List<EventAdminResponseDTO>> listPendingInvites(Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        List<EventAdminResponseDTO> invites = eventAdminService.listPendingInvites(userId);
        return ResponseEntity.ok(invites);
    }
}
