package com.meetix.meetix_api.service;

import com.meetix.meetix_api.dto.EventRequest;
import com.meetix.meetix_api.dto.EventResponse;
import com.meetix.meetix_api.dto.EventType;
import com.meetix.meetix_api.entities.*;
import com.meetix.meetix_api.repository.EventRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public EventResponse createEvent(EventRequest request) {
        Event event = createEventInstance(request.getEventType());
        mapRequestToEntity(request, event);
        Event savedEvent = eventRepository.save(event);
        return mapEntityToResponse(savedEvent);
    }

    public List<EventResponse> getAllEvents() {
        return eventRepository.findAll()
                .stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    public EventResponse getEventById(UUID id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento com ID " + id + " não encontrado."));
        return mapEntityToResponse(event);
    }

    public EventResponse updateEvent(UUID id, EventRequest request) {
        Event existingEvent = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento com ID " + id + " não encontrado."));

        if (!existingEvent.getClass().getSimpleName().equalsIgnoreCase(request.getEventType().name())) {
            throw new IllegalArgumentException("A alteração do tipo de evento não é permitida.");
        }

        mapRequestToEntity(request, existingEvent);
        Event updatedEvent = eventRepository.save(existingEvent);
        return mapEntityToResponse(updatedEvent);
    }

    public void deleteEvent(UUID id) {
        if (!eventRepository.existsById(id)) {
            throw new EntityNotFoundException("Evento com ID " + id + " não encontrado.");
        }
        eventRepository.deleteById(id);
    }

    private Event createEventInstance(EventType eventType) {
        return switch (eventType) {
            case LECTURE -> new Lecture();
            case MINI_COURSE -> new MiniCourse();
            case PARTY -> new Party();
            case WORKSHOP -> new Workshop();
            default -> throw new IllegalArgumentException("Tipo de evento inválido: " + eventType);
        };
    }

    private void mapRequestToEntity(EventRequest request, Event event) {
        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setStartDateTime(request.getStartDateTime());
        event.setEndDateTime(request.getEndDateTime());
        event.setLocation(request.getLocation());
        event.setMaxAttendees(request.getMaxAttendees());
        event.setIsPaid(request.getIsPaid());
        event.setPrice(request.getPrice());
        event.setOrganizerId(request.getOrganizerId());
    }

    private EventResponse mapEntityToResponse(Event event) {
        String eventType = event.getClass().getSimpleName().toUpperCase();

        return EventResponse.builder()
                .id(event.getId())
                .eventType(eventType)
                .title(event.getTitle())
                .description(event.getDescription())
                .startDateTime(event.getStartDateTime())
                .endDateTime(event.getEndDateTime())
                .location(event.getLocation())
                .maxAttendees(event.getMaxAttendees())
                .isPaid(event.getIsPaid())
                .price(event.getPrice())
                .organizerId(event.getOrganizerId())
                .createdAt(event.getCreatedAt())
                .updatedAt(event.getUpdatedAt())
                .build();
    }
}
