package com.railopt.ai.exception;

/**
 * Exception thrown when client-provided request data is invalid or malformed (HTTP 400).
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
