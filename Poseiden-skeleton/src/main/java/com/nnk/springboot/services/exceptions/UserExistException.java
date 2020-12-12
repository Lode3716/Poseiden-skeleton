package com.nnk.springboot.services.exceptions;

public class UserExistException extends RuntimeException{

    public UserExistException() {
    }

    public UserExistException(String message) {
        super(message);
    }
}
