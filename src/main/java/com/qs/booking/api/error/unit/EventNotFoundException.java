package com.qs.booking.api.error.unit;

public class EventNotFoundException extends CustomNotFoundException {

    public EventNotFoundException(String message, String path) {
        super(message, path);
    }
}
