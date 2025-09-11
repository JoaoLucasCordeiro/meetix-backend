package com.meetix.meetix_api.repository;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.meetix.meetix_api.entities.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID>{

}