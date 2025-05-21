package ru.otus.log;

public class MyTestLogging implements MyTestLoggingInterface {

    @Override
    public void calculation(int param) {
        System.out.println("This is one param");
    }

    @Override
    @Log
    public void calculation(int param1, int param2) {
        System.out.println("This is two params");
    }

    @Override
    @Log
    public void calculation(int param1, int param2, String param3) {
        System.out.println("This is three params");
    }
}
