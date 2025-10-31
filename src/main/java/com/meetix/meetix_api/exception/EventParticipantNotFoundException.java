package com.meetix.meetix_api.exception;

public class EventParticipantNotFoundException extends RuntimeException {
    public EventParticipantNotFoundException(String message) {
        super(message);
    }
}
