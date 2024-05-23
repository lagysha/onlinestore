package io.teamchallenge.exception;

/**
 * Exception thrown when authentication credentials are invalid.
 */
public class BadCredentialsException extends RuntimeException {
    /**
     * Constructs a new BadCredentialsException with the specified detail message.
     *
     * @param message the detail message.
     */
    public BadCredentialsException(String message) {
        super(message);
    }

    /**
     * Constructs a new BadCredentialsException with the specified detail message and cause.
     *
     * @param message the detail message.
     * @param cause   the cause.
     */
    public BadCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }
}