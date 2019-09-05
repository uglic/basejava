package ru.javawebinar.basejava.generator.loader;

import java.util.List;

public abstract class AbstractNameFileLineLoader implements IFileLineLoader<String> {
    private final String filename;

    public AbstractNameFileLineLoader(String filename) {
        this.filename = filename;
    }

    @Override
    public String parseLine(String line) {
        return line.trim();
    }

    @Override
    public List<String> load() {
        List<String> list = loadFromFile(filename);
        return list.isEmpty() ? defaultList() : list;
    }
}
