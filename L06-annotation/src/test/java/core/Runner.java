package core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;
import tests.MainTest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

public class Runner {

    Logger log = LoggerFactory.getLogger(Logger.class);

    public void run(List<Class<?>> classes) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        int unsuccessfulTestCounter = 0;
        int successfulTestCounter = 0;

        for (var clazz: classes) {
            var methods = clazz.getMethods();

            Method before = null;
            Method after = null;
            List<Method> testsMethods = new ArrayList<>();

            for (var method : methods) {
                if (method.isAnnotationPresent(Before.class)) {
                    before = method;
                }

                if (method.isAnnotationPresent(Test.class)) {
                    testsMethods.add(method);
                }

                if (method.isAnnotationPresent(After.class)) {
                    after = method;
                }
            }

            for (var method: testsMethods) {
                var mainTest = clazz.getConstructor().newInstance();
                if (!isNull(before)) {
                    Method myMethod = clazz.getMethod(before.getName());
                    myMethod.invoke(mainTest);
                }

                var testMethod = clazz.getMethod(method.getName());
                try {
                    testMethod.invoke(mainTest);
                    successfulTestCounter++;
                } catch (InvocationTargetException e) {
                    unsuccessfulTestCounter++;
                }


                if (!isNull(after)) {
                    Method myMethod = clazz.getMethod(after.getName());
                    myMethod.invoke(mainTest);
                }
            }
        }
        log.info("Was run tests. successful: {} unsuccessful: {}", successfulTestCounter, unsuccessfulTestCounter);
    }
}
