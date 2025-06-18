package ru.otus.dataprocessor;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import ru.otus.exceptions.FileProcessException;
import ru.otus.model.Measurement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

public class ResourcesFileLoader implements Loader {

    private final  List<Measurement> measurements;

    private static final Type LIST_TYPE_MEASUREMENT = new TypeToken<List<Measurement>>(){}.getType();

    public ResourcesFileLoader(String fileName) throws FileProcessException {
        try (var resourceAsStream = ResourcesFileLoader.class.getClassLoader().getResourceAsStream(fileName)) {
            if (nonNull(resourceAsStream)) {
                String resource = new BufferedReader(new InputStreamReader(
                        resourceAsStream, StandardCharsets.UTF_8))
                        .lines().collect(Collectors.joining(System.lineSeparator()));
                this.measurements = new Gson().fromJson(resource, LIST_TYPE_MEASUREMENT);
            } else throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Measurement> load() {
        //читает файл, парсит и возвращает результат
        return measurements;
    }
}
