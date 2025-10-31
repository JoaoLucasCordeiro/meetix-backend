package com.meetix.meetix_api.repositories;

import com.meetix.meetix_api.domain.event.Event;
import com.meetix.meetix_api.domain.event.EventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {
    List<Event> findByOrganizerId(UUID organizerId);
    
    List<Event> findByEventType(EventType eventType);
    
    @Query("SELECT e FROM Event e WHERE e.startDateTime >= :now ORDER BY e.startDateTime ASC")
    List<Event> findUpcomingEvents(LocalDateTime now);
    
    @Query("SELECT e FROM Event e WHERE e.startDateTime < :now ORDER BY e.startDateTime DESC")
    List<Event> findPastEvents(LocalDateTime now);
}
