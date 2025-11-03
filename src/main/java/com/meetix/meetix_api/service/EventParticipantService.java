package com.meetix.meetix_api.service;

import com.meetix.meetix_api.domain.event.Event;
import com.meetix.meetix_api.domain.event.EventParticipant;
import com.meetix.meetix_api.domain.event.EventParticipantRequestDTO;
import com.meetix.meetix_api.domain.event.EventParticipantResponseDTO;
import com.meetix.meetix_api.domain.user.User;
import com.meetix.meetix_api.exception.*;
import com.meetix.meetix_api.exception.common.ResourceNotFoundException;
import com.meetix.meetix_api.exception.common.ValidationException;
import com.meetix.meetix_api.exception.event.EventFullException;
import com.meetix.meetix_api.exception.participation.AlreadyRegisteredException;
import com.meetix.meetix_api.exception.participation.EventParticipantNotFoundException;
import com.meetix.meetix_api.repositories.EventParticipantRepository;
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
public class EventParticipantService {

    private final EventParticipantRepository participantRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Transactional
    public EventParticipantResponseDTO registerParticipant(EventParticipantRequestDTO data) {
        Event event = eventRepository.findById(data.eventId())
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado com ID: " + data.eventId()));

        User user = userRepository.findById(data.userId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + data.userId()));

        if (participantRepository.existsByEventIdAndUserId(data.eventId(), data.userId())) {
            throw new AlreadyRegisteredException("Usuário já está inscrito neste evento");
        }

        if (event.getMaxAttendees() != null) {
            long currentParticipants = participantRepository.countParticipantsByEventId(event.getId());
            if (currentParticipants >= event.getMaxAttendees()) {
                throw new EventFullException("Evento está com capacidade máxima de participantes");
            }
        }

        EventParticipant participant = new EventParticipant();
        participant.setEvent(event);
        participant.setUser(user);

        EventParticipant savedParticipant = participantRepository.save(participant);
        return EventParticipantResponseDTO.fromEntity(savedParticipant);
    }

    @Transactional
    public void cancelRegistration(UUID eventId, UUID userId) {
        EventParticipant participant = participantRepository.findByEventIdAndUserId(eventId, userId)
                .orElseThrow(() -> new EventParticipantNotFoundException(
                        "Inscrição não encontrada para este usuário neste evento"
                ));

        participantRepository.delete(participant);
    }

    @Transactional(readOnly = true)
    public List<EventParticipantResponseDTO> getEventParticipants(UUID eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new ResourceNotFoundException("Evento não encontrado com ID: " + eventId);
        }

        return participantRepository.findByEventId(eventId)
                .stream()
                .map(EventParticipantResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EventParticipantResponseDTO> getUserEvents(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Usuário não encontrado com ID: " + userId);
        }

        return participantRepository.findByUserId(userId)
                .stream()
                .map(EventParticipantResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public EventParticipantResponseDTO checkIn(UUID eventId, UUID userId) {
        EventParticipant participant = participantRepository.findByEventIdAndUserId(eventId, userId)
                .orElseThrow(() -> new EventParticipantNotFoundException(
                        "Inscrição não encontrada para este usuário neste evento"
                ));

        if (Boolean.TRUE.equals(participant.getAttended())) {
            throw new ValidationException("Check-in já realizado para este participante");
        }

        participant.setAttended(true);
        participant.setCheckedInAt(LocalDateTime.now());

        EventParticipant updatedParticipant = participantRepository.save(participant);
        return EventParticipantResponseDTO.fromEntity(updatedParticipant);
    }

    @Transactional(readOnly = true)
    public boolean isUserRegistered(UUID eventId, UUID userId) {
        return participantRepository.existsByEventIdAndUserId(eventId, userId);
    }

    @Transactional(readOnly = true)
    public long getParticipantCount(UUID eventId) {
        return participantRepository.countParticipantsByEventId(eventId);
    }

    @Transactional(readOnly = true)
    public List<EventParticipantResponseDTO> getAttendedParticipants(UUID eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new ResourceNotFoundException("Evento não encontrado com ID: " + eventId);
        }

        return participantRepository.findAttendedParticipantsByEventId(eventId)
                .stream()
                .map(EventParticipantResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
