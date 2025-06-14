package ru.otus.exceptions;

public class EvenSecondClockException extends RuntimeException {

    public EvenSecondClockException(int second) {
        super("Current second is: " + second);
    }

}