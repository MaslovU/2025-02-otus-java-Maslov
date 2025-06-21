package ru.otus.jdbc.mapper;

import ru.otus.annotatons.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private String entityClassName;
    private Constructor<?> constructor;
    private List<Field> allFields;
    private List<Field> nonIdFields;
    private Field idMarkedField;

    public EntityClassMetaDataImpl(Class<T> entityClass) {
        requireNonNull(entityClass);
        defineClassName(entityClass);
        defineFields(entityClass);
        defineClassConstructor(entityClass);
        defineAllFields(entityClass);
    }

    private void defineClassName(final Class<T> entityClass) {
        this.entityClassName = entityClass.getSimpleName();
    }

    private void defineFields(final Class<T> entityClass) {
        Field[] declaredFields = entityClass.getDeclaredFields();

        idMarkedField = stream(declaredFields)
                .filter(f -> f.isAnnotationPresent(Id.class))
                .findAny().orElseThrow(() -> new RuntimeException("No fields marked as Id"));

        nonIdFields = stream(declaredFields)
                .filter(f -> !f.isAnnotationPresent(Id.class))
                .collect(Collectors.toList());
    }

    private void defineClassConstructor(final Class<T> entityClass) {
        Constructor<?>[] constructors = entityClass.getDeclaredConstructors();
        this.constructor = stream(constructors)
                .filter(e -> e.getParameterCount() == nonIdFields.size() + 1)
                .findAny().orElseThrow(() -> new RuntimeException("Can't define correct persistence constructor"));
    }

    private void defineAllFields(final Class<T> entityClass) {
        allFields = new ArrayList<>();
        allFields.addAll(nonIdFields);
        allFields.add(idMarkedField);
    }

    @Override
    public String getName() {
        return entityClassName;
    }

    @Override
    public Constructor getConstructor() {
        return constructor;
    }

    @Override
    public Field getIdField() {
        return idMarkedField;
    }

    @Override
    public List<Field> getAllFields() {
        return allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return this.nonIdFields;
    }
}
