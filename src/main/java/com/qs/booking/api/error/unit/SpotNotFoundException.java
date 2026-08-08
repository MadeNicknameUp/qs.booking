package com.qs.booking.api.error.unit;

import lombok.Getter;

@Getter
public class SpotNotFoundException extends CustomNotFoundException {

    public SpotNotFoundException(String message) {
        super(message);
    }
}
