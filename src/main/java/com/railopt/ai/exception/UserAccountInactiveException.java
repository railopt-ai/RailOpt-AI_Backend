package com.railopt.ai.exception;

/**
 * Exception thrown when an authenticated user's account is inactive or suspended (HTTP 403).
 */
public class UserAccountInactiveException extends ForbiddenException {

    public UserAccountInactiveException(String message) {
        super(message);
    }
}
