package ru.otus.exceptions;

public class GeneralOtusException extends RuntimeException{
    public GeneralOtusException(ReflectiveOperationException e) {
    }

    public GeneralOtusException(Throwable e) {
    }
}
