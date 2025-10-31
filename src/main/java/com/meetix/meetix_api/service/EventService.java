package com.meetix.meetix_api.service;

import com.meetix.meetix_api.domain.event.Event;
import com.meetix.meetix_api.domain.event.EventRequestDTO;
import com.meetix.meetix_api.domain.event.EventResponseDTO;
import com.meetix.meetix_api.domain.event.EventUpdateDTO;
import com.meetix.meetix_api.domain.user.User;
import com.meetix.meetix_api.exception.ResourceNotFoundException;
import com.meetix.meetix_api.exception.ValidationException;
import com.meetix.meetix_api.repositories.EventRepository;
import com.meetix.meetix_api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Transactional
    public EventResponseDTO createEvent(EventRequestDTO data) {
        validateEventBusinessRules(data);

        User organizer = userRepository.findById(data.organizerId())
                .orElseThrow(() -> new ResourceNotFoundException("Organizador não encontrado com ID: " + data.organizerId()));

        Event newEvent = new Event();
        newEvent.setEventType(data.eventType());
        newEvent.setTitle(data.title());
        newEvent.setDescription(data.description());
        newEvent.setStartDateTime(data.startDateTime());
        newEvent.setEndDateTime(data.endDateTime());
        newEvent.setLocation(data.location());
        newEvent.setEventUrl(data.eventUrl());
        newEvent.setRemote(data.remote() != null ? data.remote() : false);
        newEvent.setMaxAttendees(data.maxAttendees());
        newEvent.setIsPaid(data.isPaid() != null ? data.isPaid() : false);
        newEvent.setPrice(data.price());
        newEvent.setOrganizer(organizer);
        newEvent.setGenerateCertificate(data.generateCertificate() != null ? data.generateCertificate() : false);
        newEvent.setImgUrl(data.imgUrl());

        Event savedEvent = eventRepository.save(newEvent);
        return EventResponseDTO.fromEntity(savedEvent);
    }

    @Transactional(readOnly = true)
    public List<EventResponseDTO> getAllEvents() {
        return eventRepository.findAll()
                .stream()
                .map(EventResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EventResponseDTO getEventById(UUID id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado com ID: " + id));
        return EventResponseDTO.fromEntity(event);
    }

    @Transactional(readOnly = true)
    public List<EventResponseDTO> getEventsByOrganizer(UUID organizerId) {
        if (!userRepository.existsById(organizerId)) {
            throw new ResourceNotFoundException("Organizador não encontrado com ID: " + organizerId);
        }
        return eventRepository.findByOrganizerId(organizerId)
                .stream()
                .map(EventResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EventResponseDTO> getUpcomingEvents() {
        return eventRepository.findUpcomingEvents(LocalDateTime.now())
                .stream()
                .map(EventResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public EventResponseDTO updateEvent(UUID id, EventUpdateDTO data) {
        validateEventBusinessRules(data);

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado com ID: " + id));

        event.setEventType(data.eventType());
        event.setTitle(data.title());
        event.setDescription(data.description());
        event.setStartDateTime(data.startDateTime());
        event.setEndDateTime(data.endDateTime());
        event.setLocation(data.location());
        event.setEventUrl(data.eventUrl());
        event.setRemote(data.remote() != null ? data.remote() : false);
        event.setMaxAttendees(data.maxAttendees());
        event.setIsPaid(data.isPaid() != null ? data.isPaid() : false);
        event.setPrice(data.price());
        event.setGenerateCertificate(data.generateCertificate() != null ? data.generateCertificate() : false);
        
        if (data.imgUrl() != null) {
            event.setImgUrl(data.imgUrl());
        }

        Event updatedEvent = eventRepository.save(event);
        return EventResponseDTO.fromEntity(updatedEvent);
    }

    @Transactional
    public void deleteEvent(UUID id) {
        if (!eventRepository.existsById(id)) {
            throw new ResourceNotFoundException("Evento não encontrado com ID: " + id);
        }
        eventRepository.deleteById(id);
    }

    private void validateEventBusinessRules(EventRequestDTO data) {
        if (data.endDateTime().isBefore(data.startDateTime())) {
            throw new ValidationException("Data de término deve ser posterior à data de início");
        }

        if (Boolean.TRUE.equals(data.remote())) {
            if (data.eventUrl() == null || data.eventUrl().isBlank()) {
                throw new ValidationException("URL do evento é obrigatória para eventos remotos");
            }
        } else {
            if (data.location() == null || data.location().isBlank()) {
                throw new ValidationException("Localização é obrigatória para eventos presenciais");
            }
        }

        if (Boolean.TRUE.equals(data.isPaid())) {
            if (data.price() == null || data.price().compareTo(java.math.BigDecimal.ZERO) <= 0) {
                throw new ValidationException("Preço é obrigatório e deve ser maior que zero para eventos pagos");
            }
        }
    }

    private void validateEventBusinessRules(EventUpdateDTO data) {
        if (data.endDateTime().isBefore(data.startDateTime())) {
            throw new ValidationException("Data de término deve ser posterior à data de início");
        }

        if (Boolean.TRUE.equals(data.remote())) {
            if (data.eventUrl() == null || data.eventUrl().isBlank()) {
                throw new ValidationException("URL do evento é obrigatória para eventos remotos");
            }
        } else {
            if (data.location() == null || data.location().isBlank()) {
                throw new ValidationException("Localização é obrigatória para eventos presenciais");
            }
        }

        if (Boolean.TRUE.equals(data.isPaid())) {
            if (data.price() == null || data.price().compareTo(java.math.BigDecimal.ZERO) <= 0) {
                throw new ValidationException("Preço é obrigatório e deve ser maior que zero para eventos pagos");
            }
        }
    }
}
