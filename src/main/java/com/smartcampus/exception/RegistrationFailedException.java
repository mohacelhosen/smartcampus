package com.smartcampus.exception;

public class RegistrationFailedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RegistrationFailedException() {
    }

    public RegistrationFailedException(String message) {
        super(message);
    }
}
