package com.qs.booking.api.error.unit;

public class AccountNotFoundException extends CustomNotFoundException {

    public AccountNotFoundException(String message, String path) {
        super(message, path);
    }
}
