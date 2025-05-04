package ru.otus.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

import static java.util.Objects.nonNull;

public class Ioc {
    private static final Logger logger = LoggerFactory.getLogger(Ioc.class);

    private Ioc() {
    }

    public static MyTestLoggingInterface createMyClass() {

        InvocationHandler handler = new DemoInvocationHandler(new MyTestLogging());
        return (MyTestLoggingInterface) Proxy.newProxyInstance(Ioc.class.getClassLoader(), new Class<?>[]{MyTestLoggingInterface.class}, handler);
    }

    static class DemoInvocationHandler implements InvocationHandler {
        private final MyTestLogging myClass;

        DemoInvocationHandler(MyTestLogging myClass) {
            this.myClass = myClass;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            logger.info("invoking method:{}", method);
            var logMethod = myClass.getClass().getMethod(method.getName(), method.getParameterTypes());
            if (nonNull(logMethod.getAnnotation(Log.class))) {
                System.out.println("invoking method: " + method.getName() + " params: " + Arrays.toString(args));
            }
            return method.invoke(myClass, args);
        }

        @Override
        public String toString() {
            return "DemoInvocationHandler{" + "myClass=" + myClass + '}';
        }
    }
}
