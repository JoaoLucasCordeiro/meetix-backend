package com.meetix.meetix_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.meetix.meetix_api.entities.Event;
import com.meetix.meetix_api.repository.EventRepository;

@Service
public class EventService {

    @Autowired
    private EventRepository eventoRepository;


    public Event createEvent(Event event){
        return eventoRepository.save(event);
    }

}
