package com.qs.booking.api.error.unit;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Getter
public abstract class CustomException extends RuntimeException {

    private final HttpStatus code;

    private final String message;

    private final Instant timestamp;

    private final String path;

    public CustomException(String message, String path, HttpStatus code) {
        super(message);
        this.message = message;
        this.timestamp = Instant.now();
        this.path = path;
        this.code = code;
    }
}
