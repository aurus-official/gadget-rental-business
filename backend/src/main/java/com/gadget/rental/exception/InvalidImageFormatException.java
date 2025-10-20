package com.gadget.rental.exception;

public class InvalidImageFormatException extends RuntimeException {
    public InvalidImageFormatException(String message) {
        super(message);
    }

    public InvalidImageFormatException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
