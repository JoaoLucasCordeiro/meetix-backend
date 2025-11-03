package com.meetix.meetix_api.exception.event;

public class EventFullException extends RuntimeException {
    public EventFullException(String message) {
        super(message);
    }
}
