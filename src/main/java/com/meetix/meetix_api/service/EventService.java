package com.meetix.meetix_api.service;

import com.meetix.meetix_api.domain.event.Event;
import com.meetix.meetix_api.domain.event.EventRequestDTO;
import com.meetix.meetix_api.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public Event createEvent(EventRequestDTO data) {
        Event newEvent = mapDtoToEntity(data);
        newEvent.setCreatedAt(LocalDateTime.now());
        newEvent.setUpdatedAt(LocalDateTime.now());

        if (data.image() != null) {
            String imgUrl = uploadImg(data.image());
            newEvent.setImgUrl(imgUrl);
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
        Optional<Event> optionalEvent = eventRepository.findById(id);
        if (optionalEvent.isPresent()) {
            Event eventToUpdate = optionalEvent.get();

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
            eventToUpdate.setOrganizerId(data.organizerId());
            eventToUpdate.setUpdatedAt(LocalDateTime.now());

            if (data.image() != null) {
                String imgUrl = uploadImg(data.image());
                eventToUpdate.setImgUrl(imgUrl);
            }

            return Optional.of(eventRepository.save(eventToUpdate));
        } else {
            return Optional.empty();
        }
    }

    public void deleteEvent(UUID id) {
        eventRepository.deleteById(id);
    }

    private Event mapDtoToEntity(EventRequestDTO data) {
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
        event.setOrganizerId(data.organizerId());
        return event;
    }

    private String uploadImg(MultipartFile multipartFile) {
        return "";
    }
}
