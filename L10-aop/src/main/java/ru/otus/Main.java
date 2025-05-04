package ru.otus;

import ru.otus.log.Ioc;
import ru.otus.log.MyTestLogging;
import ru.otus.log.MyTestLoggingInterface;

public class Main {
    public static void main(String[] args) {
        MyTestLoggingInterface myClass = Ioc.createMyClass();

        myClass.calculation(1);
        myClass.calculation(1, 2);
        myClass.calculation(1, 2, "3");
    }
}