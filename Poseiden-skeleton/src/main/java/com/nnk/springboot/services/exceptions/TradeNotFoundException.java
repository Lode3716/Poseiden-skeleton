package com.nnk.springboot.services.exceptions;

public class TradeNotFoundException  extends RuntimeException {


    public TradeNotFoundException() {
    }

    public TradeNotFoundException(String message) {
        super(message);
    }
}
