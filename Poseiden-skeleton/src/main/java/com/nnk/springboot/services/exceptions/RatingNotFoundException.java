package com.nnk.springboot.services.exceptions;

public class RatingNotFoundException extends RuntimeException {

    public RatingNotFoundException() {
    }

    public RatingNotFoundException(String message) {
        super(message);
    }
}
