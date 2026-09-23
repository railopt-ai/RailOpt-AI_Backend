package com.railopt.ai.exception;

/**
 * Exception thrown when a user record is not found (HTTP 404).
 */
public class UserNotFoundException extends ResourceNotFoundException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
