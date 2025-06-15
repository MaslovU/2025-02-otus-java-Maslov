package ru.otus.processor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.handler.ComplexProcessor;
import ru.otus.model.Message;

import java.time.*;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ProcessorThrowExceptionInEvenSecondTest {
    @Test
    @DisplayName("Выбрасывает исключение, если секунда на момент запуска, четная (через Clock)")
    void shouldThrowExceptionIfCurrentSecondIsEvenClock() {
        var message = new Message.Builder(1L).build();

        var oddClock = Clock.fixed(
                Instant.parse("2025-06-14T15:53:29.000Z"),
                ZoneId.systemDefault()
        );
        var complexProcessorOdd = new ComplexProcessor(List.of(new ProcessorThrowExceptionInEvenSecond(oddClock)), (ex) -> {
            throw new ProcessorThrowExceptionInEvenSecondTest.TestException(ex.getMessage());
        });

        assertThatCode(() -> complexProcessorOdd.handle(message)).doesNotThrowAnyException();

        var evenClock = Clock.fixed(
                Instant.parse("2025-06-14T15:53:30.000Z"),
                ZoneId.systemDefault()
        );
        var complexProcessorEven = new ComplexProcessor(List.of(new ProcessorThrowExceptionInEvenSecond(evenClock)), (ex) -> {
            throw new ProcessorThrowExceptionInEvenSecondTest.TestException(ex.getMessage());
        });

        assertThatThrownBy(() -> complexProcessorEven.handle(message)).hasMessage("Current second is: 30");
    }

    private static class TestException extends RuntimeException {
        public TestException(String message) {
            super(message);
        }
    }
}
