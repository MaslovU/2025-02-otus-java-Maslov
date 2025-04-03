package core;

import tests.MainTest;
import tests.NewMainTest;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {

        Runner runner = new Runner();
        Class<?> clazz = MainTest.class;
        Class<?> secondClazz = NewMainTest.class;

        List<Class<?>> classes = new ArrayList<>();
        classes.add(clazz);
        classes.add(secondClazz);

        runner.run(classes);
    }
}