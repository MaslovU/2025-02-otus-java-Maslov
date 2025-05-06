package ru.otus.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;

import static java.util.Objects.nonNull;

public class Ioc {
    private static final Logger logger = LoggerFactory.getLogger(Ioc.class);

    private Ioc() {
    }

    public static MyTestLoggingInterface createMyClass() {

        InvocationHandler handler = new DemoInvocationHandler<>(new MyTestLogging());
        return (MyTestLoggingInterface) Proxy.newProxyInstance(Ioc.class.getClassLoader(), new Class<?>[]{MyTestLoggingInterface.class}, handler);
    }

    static class DemoInvocationHandler<T extends MyTestLoggingInterface> implements InvocationHandler {
        private final T myClass;
        private final Method[] methodsList;

        DemoInvocationHandler(T t) {
            this.myClass = t;
            this.methodsList = myClass.getClass().getDeclaredMethods();
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            logger.info("invoking method:{}", method);
            for (Method value : methodsList) {
                if (Arrays.equals(method.getParameterTypes(), value.getParameterTypes())
                        && nonNull(value.getAnnotation(Log.class))) {
                    System.out.println("invoking method: " + method.getName() + " params: " + Arrays.toString(args));
                }
            }
            return method.invoke(myClass, args);
        }

        @Override
        public String toString() {
            return "DemoInvocationHandler{" + "myClass=" + myClass + '}';
        }
    }
}
