package io.teamchallenge.exception;

public class ForbiddenException extends RuntimeException {
    /**
     * Constructs a new ForbiddenException with the specified detail message.
     *
     * @param message The detail message (which is saved for later retrieval by the getMessage() method).
     */
    public ForbiddenException(String message) {
        super(message);
    }

    /**
     * Constructs a new ForbiddenException with the specified detail message and cause.
     *
     * @param message The detail message (which is saved for later retrieval by the getMessage() method).
     * @param cause   The cause (which is saved for later retrieval by the getCause() method).
     *                (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }
}