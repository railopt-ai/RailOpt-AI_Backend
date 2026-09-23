package com.railopt.ai.exception;

/**
 * Exception thrown when a user record already exists for the authenticated account or email (HTTP 409).
 */
public class UserAlreadyExistsException extends ResourceConflictException {

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
