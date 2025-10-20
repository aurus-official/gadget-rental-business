package com.gadget.rental.exception;

public class InvalidExcelFileException extends RuntimeException {
    public InvalidExcelFileException(String message) {
        super(message);
    }

    public InvalidExcelFileException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
