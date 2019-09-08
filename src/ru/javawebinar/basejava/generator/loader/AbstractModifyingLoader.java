package ru.javawebinar.basejava.generator.loader;
/*
 * @author Stepan Shcherbakov /uglic.ru/ 2019
 */

import java.util.List;

public abstract class AbstractModifyingLoader<T> implements ILoader<T> {
    private Iterable<T> items;

    public AbstractModifyingLoader(Iterable<T> items) {
        this.items = items;
    }

    @Override
    public List<T> load() {
        return load(items);
    }

    abstract List<T> load(Iterable<T> items);
}
