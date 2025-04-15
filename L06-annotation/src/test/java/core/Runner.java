package core;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.LifecycleMethodExecutionExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;
import ru.otus.exceptions.BeforeException;
import ru.otus.exceptions.GeneralOtusException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Runner implements LifecycleMethodExecutionExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(Runner.class);

    public TestResult run(List<Class<?>> classes) throws GeneralOtusException {
        int unsuccessfulTestCounter = 0;
        int successfulTestCounter = 0;

        for (var clazz : classes) {
            var methods = clazz.getMethods();

            List<Method> beforeList = new ArrayList<>();
            List<Method> afterList = new ArrayList<>();
            List<Method> testsMethods = new ArrayList<>();

            for (var method : methods) {
                if (method.isAnnotationPresent(Before.class)) {
                    beforeList.add(method);
                }

                if (method.isAnnotationPresent(Test.class)) {
                    testsMethods.add(method);
                }

                if (method.isAnnotationPresent(After.class)) {
                    afterList.add(method);
                }
            }

            try {
                for (var method : testsMethods) {
                    var mainTest = clazz.getConstructor().newInstance();
                    for (var before : beforeList) {
                        try {
                            Method myMethod = clazz.getMethod(before.getName());
                            myMethod.invoke(mainTest);
                        } catch (InvocationTargetException e) {
                            throw new BeforeException("Got exception for Before method in class: " + clazz.getName());
                        }
                    }

                    var testMethod = clazz.getMethod(method.getName());
                    try {
                        testMethod.invoke(mainTest);
                        successfulTestCounter++;
                    } catch (InvocationTargetException e) {
                        unsuccessfulTestCounter++;
                    }

                    for (var after : afterList) {
                        Method myMethod = clazz.getMethod(after.getName());
                        myMethod.invoke(mainTest);
                    }
                }
            }  catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException ex) {
                throw new GeneralOtusException(ex);
            } catch (BeforeException ex) {
                log.info(ex.getMessage());
            }
        }
        var total = successfulTestCounter + unsuccessfulTestCounter;
    return new TestResult(total, successfulTestCounter, unsuccessfulTestCounter);
    }
}
