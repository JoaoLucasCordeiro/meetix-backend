package com.meetix.meetix_api.exception.participation;

public class EventParticipantNotFoundException extends RuntimeException {
    public EventParticipantNotFoundException(String message) {
        super(message);
    }
}
