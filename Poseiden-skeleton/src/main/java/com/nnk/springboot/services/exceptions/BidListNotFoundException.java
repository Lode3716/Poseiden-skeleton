package com.nnk.springboot.services.exceptions;

public class BidListNotFoundException extends RuntimeException {


    public BidListNotFoundException() {
    }

    public BidListNotFoundException(String message) {
        super(message);
    }
}
