package ru.otus.processor;

import ru.otus.model.Message;

public class ProcessorThrowExceptionInEvenSecond implements Processor{
    @Override
    public Message process(Message message) {
        return null;
    }
}
