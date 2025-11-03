package com.meetix.meetix_api.service;

import com.meetix.meetix_api.domain.event.Event;
import com.meetix.meetix_api.domain.event.EventAdmin;
import com.meetix.meetix_api.domain.event.EventAdminInviteRequestDTO;
import com.meetix.meetix_api.domain.event.EventAdminResponseDTO;
import com.meetix.meetix_api.domain.user.User;
import com.meetix.meetix_api.exception.common.PermissionDeniedException;
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

    /**
     * Verifica se usuário é criador OU admin aceito do evento
     */
    public boolean isEventAdminOrCreator(UUID eventId, UUID userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Evento não encontrado"));

        // Verifica se é o criador
        if (event.getOrganizer().getId().equals(userId)) {
            return true;
        }

        // Verifica se é admin aceito
        return eventAdminRepository.existsByEventIdAndUserIdAndAcceptedTrue(eventId, userId);
    }

    /**
     * Convida usuário para ser admin do evento
     */
    @Transactional
    public EventAdminResponseDTO inviteAdmin(UUID eventId, EventAdminInviteRequestDTO request, UUID inviterId) {
        // ✅ VALIDAÇÃO DE PERMISSÃO (agora lança PermissionDeniedException)
        if (!isEventAdminOrCreator(eventId, inviterId)) {
            throw new PermissionDeniedException("Apenas admins ou criador do evento podem convidar outros admins");
        }

        // Buscar evento
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Evento não encontrado"));

        // Buscar usuário a ser convidado pelo email
        User userToInvite = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UserNotFoundException("Usuário com email " + request.email() + " não encontrado"));

        // Validar que não é o próprio criador (validação de negócio)
        if (event.getOrganizer().getId().equals(userToInvite.getId())) {
            throw new ValidationException("Criador do evento já é admin automaticamente");
        }

        // Validar que usuário ainda não foi convidado (validação de negócio)
        if (eventAdminRepository.existsByEventIdAndUserId(eventId, userToInvite.getId())) {
            throw new ValidationException("Este usuário já foi convidado para ser admin deste evento");
        }

        // Buscar quem está convidando
        User inviter = userRepository.findById(inviterId)
                .orElseThrow(() -> new UserNotFoundException("Usuário convidador não encontrado"));

        // Criar convite
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

    /**
     * Aceitar convite para ser admin
     */
    @Transactional
    public EventAdminResponseDTO acceptInvite(UUID eventId, UUID userId) {
        EventAdmin eventAdmin = eventAdminRepository.findByEventIdAndUserId(eventId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Convite não encontrado"));

        // Validação de negócio
        if (eventAdmin.getAccepted()) {
            throw new ValidationException("Convite já foi aceito");
        }

        eventAdmin.setAccepted(true);
        eventAdmin.setAcceptedAt(LocalDateTime.now());

        eventAdmin = eventAdminRepository.save(eventAdmin);

        return EventAdminResponseDTO.fromEntity(eventAdmin);
    }

    /**
     * Recusar convite
     */
    @Transactional
    public void declineInvite(UUID eventId, UUID userId) {
        EventAdmin eventAdmin = eventAdminRepository.findByEventIdAndUserId(eventId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Convite não encontrado"));

        eventAdminRepository.delete(eventAdmin);
    }

    /**
     * Remover admin do evento (apenas criador pode fazer)
     */
    @Transactional
    public void removeAdmin(UUID eventId, UUID adminId, UUID requesterId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Evento não encontrado"));

        // ✅ VALIDAÇÃO DE PERMISSÃO (agora lança PermissionDeniedException)
        if (!event.getOrganizer().getId().equals(requesterId)) {
            throw new PermissionDeniedException("Apenas o criador do evento pode remover admins");
        }

        EventAdmin eventAdmin = eventAdminRepository.findByEventIdAndUserId(eventId, adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin não encontrado"));

        eventAdminRepository.delete(eventAdmin);
    }

    /**
     * Listar admins de um evento
     */
    public List<EventAdminResponseDTO> listEventAdmins(UUID eventId) {
        return eventAdminRepository.findByEventIdAndAcceptedTrue(eventId)
                .stream()
                .map(EventAdminResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Listar convites pendentes de um usuário
     */
    public List<EventAdminResponseDTO> listPendingInvites(UUID userId) {
        return eventAdminRepository.findByUserIdAndAcceptedFalse(userId)
                .stream()
                .map(EventAdminResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
