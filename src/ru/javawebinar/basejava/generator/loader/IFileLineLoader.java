package ru.javawebinar.basejava.generator.loader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public interface IFileLineLoader<T> extends ILoader<T> {
    /**
     * Convert text line to requires type
     *
     * @param line Text line, usually from file; without end-line symbols
     * @return Value of parameter type
     */
    T parseLine(String line);

    default String filterRegex() {
        return "[а-яА-Я]*";
    }

    default List<T> loadFromFile(String filename) {
        final List<T> list = new ArrayList<>();
        final String regex = filterRegex();
        String realName = IFileLineLoader.class.getResource(filename).getFile();
        try {
            Files.lines(Paths.get(realName), StandardCharsets.UTF_8).forEach((e) -> {
                if (!e.isEmpty() && e.matches(regex)) {
                    list.add(parseLine(e));
                }
            });
        } catch (IOException ignored) {
        }
        return list;
    }
}
