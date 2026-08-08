package com.qs.booking.api.error.unit;

public class BookingNotFoundException extends CustomNotFoundException {

    public BookingNotFoundException(String message, String path) {
        super(message, path);
    }
}
