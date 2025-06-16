package ru.otus.model;

import java.util.List;

import static java.util.Objects.nonNull;

public class ObjectForMessage {

    private List<String> data;

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ObjectForMessage{" +
                "data=" + data +
                '}';
    }

    public static ObjectForMessage cloneObject(ObjectForMessage objectForMessage) {
        ObjectForMessage cloneableObject = new ObjectForMessage();

        if (nonNull(objectForMessage) && nonNull(objectForMessage.getData())) {
            cloneableObject.setData(List.copyOf(objectForMessage.getData()));
        }

        return cloneableObject;
    }
}
