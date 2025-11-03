package com.meetix.meetix_api.service;

import com.meetix.meetix_api.domain.event.Event;
import com.meetix.meetix_api.domain.event.EventAdmin;
import com.meetix.meetix_api.domain.event.EventAdminInviteRequestDTO;
import com.meetix.meetix_api.domain.event.EventAdminResponseDTO;
import com.meetix.meetix_api.domain.user.User;
import com.meetix.meetix_api.exception.common.ResourceNotFoundException;
import com.meetix.meetix_api.exception.common.ValidationException;
import com.meetix.meetix_api.exception.event.EventNotFoundException;
import com.meetix.meetix_api.exception.user.UserNotFoundException;
import com.meetix.meetix_api.repositories.EventAdminRepository;
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
public class EventAdminService {

    private final EventAdminRepository eventAdminRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

  
    public boolean isEventAdminOrCreator(UUID eventId, UUID userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Evento não encontrado"));

        if (event.getOrganizer().getId().equals(userId)) {
            return true;
        }

        return eventAdminRepository.existsByEventIdAndUserIdAndAcceptedTrue(eventId, userId);
    }

   
    @Transactional
    public EventAdminResponseDTO inviteAdmin(UUID eventId, EventAdminInviteRequestDTO request, UUID inviterId) {
        if (!isEventAdminOrCreator(eventId, inviterId)) {
            throw new ValidationException("Apenas admins ou criador do evento podem convidar outros admins");
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Evento não encontrado"));

        User userToInvite = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UserNotFoundException("Usuário com email " + request.email() + " não encontrado"));

        if (event.getOrganizer().getId().equals(userToInvite.getId())) {
            throw new ValidationException("Criador do evento já é admin automaticamente");
        }

        if (eventAdminRepository.existsByEventIdAndUserId(eventId, userToInvite.getId())) {
            throw new ValidationException("Este usuário já foi convidado para ser admin deste evento");
        }

        User inviter = userRepository.findById(inviterId)
                .orElseThrow(() -> new UserNotFoundException("Usuário convidador não encontrado"));

        EventAdmin eventAdmin = new EventAdmin();
        eventAdmin.setEvent(event);
        eventAdmin.setUser(userToInvite);
        eventAdmin.setInvitedBy(inviter);
        eventAdmin.setInvitedAt(LocalDateTime.now());
        eventAdmin.setAccepted(false);

        eventAdmin = eventAdminRepository.save(eventAdmin);

        // TODO: Enviar notificação para o usuário convidado

        return EventAdminResponseDTO.fromEntity(eventAdmin);
    }

 
    @Transactional
    public EventAdminResponseDTO acceptInvite(UUID eventId, UUID userId) {
        EventAdmin eventAdmin = eventAdminRepository.findByEventIdAndUserId(eventId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Convite não encontrado"));

        if (eventAdmin.getAccepted()) {
            throw new ValidationException("Convite já foi aceito");
        }

        eventAdmin.setAccepted(true);
        eventAdmin.setAcceptedAt(LocalDateTime.now());

        eventAdmin = eventAdminRepository.save(eventAdmin);

        return EventAdminResponseDTO.fromEntity(eventAdmin);
    }

  
    @Transactional
    public void declineInvite(UUID eventId, UUID userId) {
        EventAdmin eventAdmin = eventAdminRepository.findByEventIdAndUserId(eventId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Convite não encontrado"));

        eventAdminRepository.delete(eventAdmin);
    }

  
    @Transactional
    public void removeAdmin(UUID eventId, UUID adminId, UUID requesterId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Evento não encontrado"));

        if (!event.getOrganizer().getId().equals(requesterId)) {
            throw new ValidationException("Apenas o criador do evento pode remover admins");
        }

        EventAdmin eventAdmin = eventAdminRepository.findByEventIdAndUserId(eventId, adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin não encontrado"));

        eventAdminRepository.delete(eventAdmin);
    }

   
    public List<EventAdminResponseDTO> listEventAdmins(UUID eventId) {
        return eventAdminRepository.findByEventIdAndAcceptedTrue(eventId)
                .stream()
                .map(EventAdminResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

   
    public List<EventAdminResponseDTO> listPendingInvites(UUID userId) {
        return eventAdminRepository.findByUserIdAndAcceptedFalse(userId)
                .stream()
                .map(EventAdminResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
