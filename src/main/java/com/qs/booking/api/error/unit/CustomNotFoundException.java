package com.qs.booking.api.error.unit;

import org.springframework.http.HttpStatus;

public abstract class CustomNotFoundException extends CustomException {

    public CustomNotFoundException(String message, String path) {
        super(message, path, HttpStatus.NOT_FOUND);
    }
}
