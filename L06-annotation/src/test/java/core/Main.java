package core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tests.MainTest;
import tests.NewMainTest;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        Logger log = LoggerFactory.getLogger(Logger.class);

        Runner runner = new Runner();
        Class<?> clazz = MainTest.class;
        Class<?> secondClazz = NewMainTest.class;

        List<Class<?>> classes = new ArrayList<>();
        classes.add(clazz);
        classes.add(secondClazz);

        TestResult result = runner.run(classes);

        log.info("Was run {} tests. successful: {} unsuccessful: {}",
                result.getTotal(), result.getSuccessfulTestCounter(), result.getUnsuccessfulTestCounter());
    }
}
