package com.qs.booking.api.error.unit;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class EventNotFoundException extends RuntimeException {

    private final Integer code;

    private final String message;

    public EventNotFoundException(String message) {
        super(message);
        this.message = message;
        this.code = HttpStatus.NOT_FOUND.value();
    }
}
