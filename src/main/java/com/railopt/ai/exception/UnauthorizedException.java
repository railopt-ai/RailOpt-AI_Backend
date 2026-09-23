package com.railopt.ai.exception;

/**
 * Exception thrown when authentication credentials / Firebase token are missing or invalid (HTTP 401).
 */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}
