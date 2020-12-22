package com.nnk.springboot.services.exceptions;

public class RuleNameNotFoundException extends RuntimeException {


    public RuleNameNotFoundException() {
    }

    public RuleNameNotFoundException(String message) {
        super(message);
    }
}
