package ru.otus.processor;

import ru.otus.exceptions.EvenSecondClockException;
import ru.otus.model.Message;

import java.time.Clock;
import java.time.LocalTime;

public class ProcessorThrowExceptionInEvenSecond implements Processor {

    private final Clock clock;

    public ProcessorThrowExceptionInEvenSecond(Clock clock) {
        this.clock = clock;
    }

    public ProcessorThrowExceptionInEvenSecond() {
        this(Clock.systemDefaultZone());
    }

    @Override
    public Message process(Message message) {
        int currentSecond = LocalTime.now(clock).getSecond();
        if (currentSecond % 2 == 0) {
            throw new EvenSecondClockException(currentSecond);
        }
        return message.toBuilder().build();
    }
}
