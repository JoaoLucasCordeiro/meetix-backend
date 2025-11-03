package com.meetix.meetix_api.repositories;

import com.meetix.meetix_api.domain.event.EventAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventAdminRepository extends JpaRepository<EventAdmin, UUID> {

    List<EventAdmin> findByEventIdAndAcceptedTrue(UUID eventId);

    List<EventAdmin> findByUserIdAndAcceptedFalse(UUID userId);

    boolean existsByEventIdAndUserIdAndAcceptedTrue(UUID eventId, UUID userId);

    boolean existsByEventIdAndUserId(UUID eventId, UUID userId);

    Optional<EventAdmin> findByEventIdAndUserId(UUID eventId, UUID userId);

    @Query("SELECT COUNT(ea) FROM EventAdmin ea WHERE ea.event.id = :eventId AND ea.accepted = true")
    long countAdminsByEventId(@Param("eventId") UUID eventId);
}
