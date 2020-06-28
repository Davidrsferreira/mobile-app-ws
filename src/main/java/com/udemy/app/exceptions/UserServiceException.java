package com.udemy.app.exceptions;

public class UserServiceException extends RuntimeException{

    private static final long serialVersionUID = 8774121568381763541L;

    public UserServiceException(String message) {
        super(message);
    }
}
