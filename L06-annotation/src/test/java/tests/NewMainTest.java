package tests;

import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;

public class NewMainTest {

    int count = 0;

    @Before
    public void init() {
        System.out.println("My second init method");
    }

    @After
    public void shoutDown() {
        System.out.println("My second shoutDown method");
    }

    @Test
    public void testException() {
        throw new RuntimeException();
    }

    @Test
    public void testString() {
        System.out.println("This is second test string");
    }

    @Test
    public void testInteger() {
        System.out.println("This is second test int");
    }
}
