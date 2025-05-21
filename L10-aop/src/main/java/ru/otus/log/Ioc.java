package ru.otus.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

public class Ioc {
    private static final Logger logger = LoggerFactory.getLogger(Ioc.class);

    private Ioc() {
    }

    public static MyTestLoggingInterface createMyClass() {

        InvocationHandler handler = new DemoInvocationHandler<>(new MyTestLogging());
        return (MyTestLoggingInterface) Proxy.newProxyInstance(Ioc.class.getClassLoader(), new Class<?>[]{MyTestLoggingInterface.class}, handler);
    }

    static class DemoInvocationHandler<T> implements InvocationHandler {
        private final T myClass;
        private final Set<Method> methodsList;

        DemoInvocationHandler(T t) {
            this.myClass = t;
            this.methodsList = Arrays.stream(this.myClass.getClass().getMethods())
                    .filter(method -> nonNull(method.getAnnotation(Log.class)))
                    .flatMap(method -> {
                        return Arrays.stream(this.myClass.getClass().getInterfaces())
                                .flatMap(intf -> Arrays.stream(intf.getMethods()))
                                .filter(m -> method.getName().equals(m.getName())
                                        && method.getReturnType().equals(m.getReturnType())
                                        && equalParamTypes(method.getParameterTypes(), m.getParameterTypes()));
                    })
                    .collect(Collectors.toSet());
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            logger.info("invoking method:{}", method);
            if (methodsList.contains(method)) {
                System.out.println("invoking method: " + method.getName() + " params: " + Arrays.toString(args));
            }
            return method.invoke(myClass, args);
        }

        @Override
        public String toString() {
            return "DemoInvocationHandler{" + "myClass=" + myClass + '}';
        }

        private boolean equalParamTypes(Class<?>[] params1, Class<?>[] params2) {
            if (params1.length == params2.length) {
                for (int i = 0; i < params1.length; i++) {
                    if (params1[i] != params2[i])
                        return false;
                }
                return true;
            }
            return false;
        }
    }
}
