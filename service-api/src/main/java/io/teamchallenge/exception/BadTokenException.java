package io.teamchallenge.exception;

/**
 * Exception indicating a bad or invalid token.
 */
public class BadTokenException extends RuntimeException {
    /**
     * Constructs a new BadTokenException with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method)
     */
    public BadTokenException(String message) {
        super(message);
    }

    /**
     * Constructs a new BadTokenException with the specified detail message and cause.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method)
     * @param cause   the cause (which is saved for later retrieval by the {@link #getCause()} method)
     */
    public BadTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}