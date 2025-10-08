package com.meetix.meetix_api.repositories;

import com.meetix.meetix_api.domain.event.EventParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventParticipantRepository extends JpaRepository<EventParticipant, UUID> {
    
    @Query("SELECT ep FROM EventParticipant ep WHERE ep.event.id_event = :eventId")
    List<EventParticipant> findByEventId(@Param("eventId") UUID eventId);
    
    @Query("SELECT ep FROM EventParticipant ep WHERE ep.user.id_user = :userId")
    List<EventParticipant> findByUserId(@Param("userId") UUID userId);
    
    @Query("SELECT ep FROM EventParticipant ep WHERE ep.event.id_event = :eventId AND ep.user.id_user = :userId")
    Optional<EventParticipant> findByEventIdAndUserId(@Param("eventId") UUID eventId, @Param("userId") UUID userId);
    
    @Query("SELECT COUNT(ep) > 0 FROM EventParticipant ep WHERE ep.event.id_event = :eventId AND ep.user.id_user = :userId")
    boolean existsByEventIdAndUserId(@Param("eventId") UUID eventId, @Param("userId") UUID userId);
}
