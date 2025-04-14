package tests;

import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;

public class MainTest {

    @Before
    public void init() {
        System.out.println("My init method");
        throw new RuntimeException("error");
    }

    @After
    public void shoutDown() {
        System.out.println("My shoutDown method");
    }

    @Test
    public void testException() {
        throw new RuntimeException();
    }

    @Test
    public void testString() {
        System.out.println("This is test string");
    }

    @Test
    public void testInteger() {
        System.out.println("This is test int");
    }
}
