package com.abubakir.timekeeper.app.exception;

public class InvalidDateTimeException extends RuntimeException {
    public InvalidDateTimeException(final String message) {
        super(message);
    }
}
