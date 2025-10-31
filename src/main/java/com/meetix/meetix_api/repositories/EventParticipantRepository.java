package com.meetix.meetix_api.repositories;

import com.meetix.meetix_api.domain.event.EventParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventParticipantRepository extends JpaRepository<EventParticipant, UUID> {
    
    List<EventParticipant> findByEventId(UUID eventId);
    
    List<EventParticipant> findByUserId(UUID userId);
    
    Optional<EventParticipant> findByEventIdAndUserId(UUID eventId, UUID userId);
    
    boolean existsByEventIdAndUserId(UUID eventId, UUID userId);
    
    @Query("SELECT COUNT(ep) FROM EventParticipant ep WHERE ep.event.id = :eventId")
    long countParticipantsByEventId(UUID eventId);
    
    @Query("SELECT ep FROM EventParticipant ep WHERE ep.event.id = :eventId AND ep.attended = true")
    List<EventParticipant> findAttendedParticipantsByEventId(UUID eventId);
}
