package com.nnk.springboot.services.exceptions;

public class CurvePointNotFoundException extends RuntimeException  {

    public CurvePointNotFoundException() {
    }

    public CurvePointNotFoundException(String message) {
        super(message);
    }
}
