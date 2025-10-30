package com.meetix.meetix_api.service;

import com.meetix.meetix_api.domain.event.Event;
import com.meetix.meetix_api.domain.event.EventRequestDTO;
import com.meetix.meetix_api.domain.user.User;
import com.meetix.meetix_api.repositories.EventRepository;
import com.meetix.meetix_api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;
// ver logica de service e repository
    public Event createEvent(EventRequestDTO data) {
        validateEventData(data);

        User organizer = userRepository.findById(data.organizerId())
                .orElseThrow(() -> new IllegalArgumentException("Organizer not found"));

        Event newEvent = mapDtoToEntity(data, organizer);
        newEvent.setCreatedAt(LocalDateTime.now());
        newEvent.setUpdatedAt(LocalDateTime.now());

        if (data.imgUrl() != null && !data.imgUrl().isEmpty()) {
            newEvent.setImgUrl(data.imgUrl());
        } else {
            newEvent.setImgUrl("https://placeholder.com/default-image.png");
        }

        return eventRepository.save(newEvent);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Optional<Event> getEventById(UUID id) {
        return eventRepository.findById(id);
    }

    public Optional<Event> updateEvent(UUID id, EventRequestDTO data) {
        validateEventData(data);

        Optional<Event> optionalEvent = eventRepository.findById(id);
        if (optionalEvent.isPresent()) {
            Event eventToUpdate = optionalEvent.get();

            User organizer = userRepository.findById(data.organizerId())
                    .orElseThrow(() -> new IllegalArgumentException("Organizer not found"));

            eventToUpdate.setEventType(data.eventType());
            eventToUpdate.setTitle(data.title());
            eventToUpdate.setDescription(data.description());
            eventToUpdate.setStartDateTime(data.startDateTime());
            eventToUpdate.setEndDateTime(data.endDateTime());
            eventToUpdate.setLocation(data.location());
            eventToUpdate.setEventUrl(data.eventUrl());
            eventToUpdate.setRemote(data.remote());
            eventToUpdate.setMaxAttendees(data.maxAttendees());
            eventToUpdate.setIsPaid(data.isPaid());
            eventToUpdate.setPrice(data.price());
            eventToUpdate.setOrganizer(organizer);
            eventToUpdate.setGenerateCertificate(data.generateCertificate());
            eventToUpdate.setUpdatedAt(LocalDateTime.now());

            if (data.imgUrl() != null && !data.imgUrl().isEmpty()) {
                eventToUpdate.setImgUrl(data.imgUrl());
            }

            return Optional.of(eventRepository.save(eventToUpdate));
        } else {
            return Optional.empty();
        }
    }

    public void deleteEvent(UUID id) {
        eventRepository.deleteById(id);
    }

    private void validateEventData(EventRequestDTO data) {
        if (data.remote() != null && data.remote()) {
            if (data.eventUrl() == null || data.eventUrl().isBlank()) {
                throw new IllegalArgumentException("Event URL must be provided for remote events");
            }
        } else {
            if (data.location() == null || data.location().isBlank()) {
                throw new IllegalArgumentException("Location must be provided for non-remote events");
            }
        }
        if (data.organizerId() == null) {
            throw new IllegalArgumentException("Organizer ID is required");
        }
    }

    private Event mapDtoToEntity(EventRequestDTO data, User organizer) {
        Event event = new Event();
        event.setEventType(data.eventType());
        event.setTitle(data.title());
        event.setDescription(data.description());
        event.setStartDateTime(data.startDateTime());
        event.setEndDateTime(data.endDateTime());
        event.setLocation(data.location());
        event.setEventUrl(data.eventUrl());
        event.setRemote(data.remote());
        event.setMaxAttendees(data.maxAttendees());
        event.setIsPaid(data.isPaid());
        event.setPrice(data.price());
        event.setOrganizer(organizer);
        event.setGenerateCertificate(data.generateCertificate());
        event.setImgUrl(data.imgUrl());
        return event;
    }
}
