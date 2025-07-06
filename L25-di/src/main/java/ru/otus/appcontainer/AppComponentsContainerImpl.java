package ru.otus.appcontainer;

import lombok.SneakyThrows;
import org.reflections.Reflections;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    protected AppComponentsContainerImpl() {
    }

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    public AppComponentsContainerImpl(Class<?>... initialConfigClass) {
        for (Class<?> configClass : initialConfigClass) {
            checkConfigClass(configClass);
        }

        var configClasses = new ArrayList<>(Arrays.stream(initialConfigClass)
                .filter(m -> m.isAnnotationPresent(AppComponentsContainerConfig.class))
                .sorted(Comparator.comparingInt(config -> config.getAnnotation(AppComponentsContainerConfig.class).order()))
                .toList());

        for (Class<?> configClass : configClasses) {
            processConfig(configClass);
        }
    }

    public AppComponentsContainerImpl(String packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(AppComponentsContainerConfig.class);

        for (Class<?> configClass : classes) {
            processConfig(configClass);
        }
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);

        List<Method> configMethods = Arrays.stream(configClass.getMethods())
                .filter(m -> m.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparingInt(method -> method.getAnnotation(AppComponent.class).order()))
                .toList();

        for (Method method : configMethods) {
            try {
                Parameter[] parameters = method.getParameters();
                Object[] componentParams = new Object[parameters.length];
                String name = method.getAnnotation(AppComponent.class).name();

                if (appComponentsByName.containsKey(name)) {
                    throw new RuntimeException("Duplicated appComponent name: " + name);
                }

                for (int i = 0; i < parameters.length; i++) {
                    Parameter parameter = parameters[i];
                    Class<?> type = parameter.getType();
                    Object componentParam = getAppComponent(type);
                    componentParams[i] = componentParam;
                }

                Object component = method.invoke(configClass.getConstructor().newInstance(), componentParams);

                appComponents.add(component);
                appComponentsByName.put(name, component);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    @SneakyThrows
    public <C> C getAppComponent(Class<C> componentClass) {
        List<Object> components = appComponents.stream()
                .filter(component -> componentClass.isAssignableFrom(component.getClass()))
                .toList();

        if (components.isEmpty()) {
            throw new Exception("No component found for class " + componentClass.getName());
        }

        if (components.size() > 1) {
            throw new Exception("Multiple component found for class " + componentClass.getName());
        }

        for (Object appComponent : appComponents) {
            if (componentClass.isInstance(appComponent)) {
                return (C) appComponent;
            }
        }

        throw new IllegalArgumentException("Can't find appComponent with class: \"" + componentClass.getName() + "\"");
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(String componentName) {
        if (appComponentsByName.containsKey(componentName)) {
            return (C) appComponentsByName.get(componentName);
        }

        throw new IllegalArgumentException("Can't find appComponent with name: \"" + componentName + "\"");
    }
}
