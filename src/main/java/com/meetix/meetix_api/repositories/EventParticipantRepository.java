package com.meetix.meetix_api.repositories;

import com.meetix.meetix_api.domain.event.EventParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventParticipantRepository extends JpaRepository<EventParticipant, UUID> {
    List<EventParticipant> findByEventId_event(UUID eventId);
    List<EventParticipant> findByUserId_user(UUID userId);
    Optional<EventParticipant> findByEventId_eventAndUserId_user(UUID eventId, UUID userId);
    boolean existsByEventId_eventAndUserId_user(UUID eventId, UUID userId);
}
