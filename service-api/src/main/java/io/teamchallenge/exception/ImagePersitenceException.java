package io.teamchallenge.exception;

public class ImagePersitenceException extends RuntimeException{

    public ImagePersitenceException(String message) {
        super(message);
    }

    public ImagePersitenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
