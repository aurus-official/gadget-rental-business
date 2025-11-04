package com.gadget.rental.exception;

public class ImageCountExceededException extends RuntimeException {
    public ImageCountExceededException(String message) {
        super(message);
    }

    public ImageCountExceededException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
