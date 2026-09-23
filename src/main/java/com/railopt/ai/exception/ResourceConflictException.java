package com.railopt.ai.exception;

/**
 * Base exception for conflict with current resource state or uniqueness violations (HTTP 409).
 */
public class ResourceConflictException extends RuntimeException {

    public ResourceConflictException(String message) {
        super(message);
    }

    public ResourceConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}
