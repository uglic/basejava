package ru.javawebinar.basejava.generator.loader;
/*
 * @author Stepan Shcherbakov /uglic.ru/ 2019
 */

import java.util.Collections;
import java.util.List;

public abstract class AbstractNameFileLineLoader implements IFileLineLoader<String> {
    private final String filename;

    public AbstractNameFileLineLoader(String filename) {
        this.filename = filename;
    }

    @Override
    public List<String> parseLine(String line) {
        return Collections.singletonList(line.trim());
    }

    @Override
    public List<String> load() {
        List<String> list = loadFromFile(filename);
        return list.isEmpty() ? defaultList() : list;
    }
}
