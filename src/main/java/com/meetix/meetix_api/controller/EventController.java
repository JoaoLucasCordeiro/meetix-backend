package com.meetix.meetix_api.controller;

import com.meetix.meetix_api.domain.event.EventRequestDTO;
import com.meetix.meetix_api.domain.event.EventResponseDTO;
import com.meetix.meetix_api.domain.event.EventUpdateDTO;
import com.meetix.meetix_api.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequestMapping("/api/events")
@RequiredArgsConstructor
@Tag(name = "Eventos", description = "Gerenciamento de eventos acadêmicos")
public class EventController {

    private final EventService eventService;

    @PostMapping
    @Operation(summary = "Criar evento", description = "Cria um novo evento")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<EventResponseDTO> createEvent(@Valid @RequestBody EventRequestDTO data) {
        EventResponseDTO event = eventService.createEvent(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(event);
    }

    @GetMapping
    @Operation(summary = "Listar eventos", description = "Retorna lista de todos os eventos")
    public ResponseEntity<List<EventResponseDTO>> getAllEvents() {
        List<EventResponseDTO> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/upcoming")
    @Operation(summary = "Listar eventos futuros", description = "Retorna eventos que ainda vão acontecer")
    public ResponseEntity<List<EventResponseDTO>> getUpcomingEvents() {
        List<EventResponseDTO> events = eventService.getUpcomingEvents();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar evento por ID", description = "Retorna detalhes de um evento específico")
    public ResponseEntity<EventResponseDTO> getEventById(@PathVariable UUID id) {
        EventResponseDTO event = eventService.getEventById(id);
        return ResponseEntity.ok(event);
    }

    @GetMapping("/organizer/{organizerId}")
    @Operation(summary = "Listar eventos por organizador", description = "Retorna eventos criados por um usuário")
    public ResponseEntity<List<EventResponseDTO>> getEventsByOrganizer(@PathVariable UUID organizerId) {
        List<EventResponseDTO> events = eventService.getEventsByOrganizer(organizerId);
        return ResponseEntity.ok(events);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar evento", description = "Atualiza dados de um evento. Apenas criador ou admins podem editar.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evento atualizado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Sem permissão para editar este evento"),
            @ApiResponse(responseCode = "404", description = "Evento não encontrado")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<EventResponseDTO> updateEvent(
            @PathVariable UUID id,
            @Valid @RequestBody EventUpdateDTO data,
            Authentication authentication 
    ) {
        UUID userId = UUID.fromString(authentication.getName()); 
        EventResponseDTO event = eventService.updateEvent(id, data, userId);
        return ResponseEntity.ok(event);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar evento", description = "Remove um evento do sistema. Apenas o criador pode deletar.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Evento deletado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Apenas o criador pode deletar este evento"),
            @ApiResponse(responseCode = "404", description = "Evento não encontrado")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> deleteEvent(
            @PathVariable UUID id,
            Authentication authentication 
    ) {
        UUID userId = UUID.fromString(authentication.getName()); 
        eventService.deleteEvent(id, userId); 
        return ResponseEntity.noContent().build();
    }
}
