package ru.javawebinar.basejava.generator;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Lazy loading data by loader
 * @param <T> Type of list's values
 */
public class LazyList<T> implements Iterable<T> {
    private List<T> list;
    private final Supplier<List<T>> loader;

    public LazyList(Supplier<List<T>> loader) {
        this.loader = loader;
    }

    public T get(int index) {
        return getList().get(index);
    }

    public int size() {
        return getList().size();
    }

    private List<T> getList() {
        if (list == null) {
            synchronized (this) {
                if (list == null) {
                    list = loader.get();
                }
            }
        }
        return list;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int i;

            @Override
            public boolean hasNext() {
                return getList().size() > i;
            }

            @Override
            public T next() {
                return getList().get(i++);
            }
        };
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        getList().forEach(action);
    }
}
