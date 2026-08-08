package com.qs.booking.api.error.unit;

import org.springframework.http.HttpStatus;

public class InvalidParameterException extends CustomException {

    public InvalidParameterException(String message, String path) {
        super(message, path, HttpStatus.BAD_REQUEST);
    }
}
