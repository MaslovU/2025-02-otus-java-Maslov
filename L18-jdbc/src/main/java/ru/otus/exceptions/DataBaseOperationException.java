package ru.otus.exceptions;

public class DataBaseOperationException extends RuntimeException {
    public DataBaseOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataBaseOperationException(Exception e) {
        super(e);
    }
}
