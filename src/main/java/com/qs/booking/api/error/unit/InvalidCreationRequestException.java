package com.qs.booking.api.error.unit;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InvalidCreationRequestException extends RuntimeException {

    private final Integer code;

    private final String message;

    public InvalidCreationRequestException(String message) {
        super(message);
        this.message = message;
        this.code = HttpStatus.BAD_REQUEST.value();
    }
}
