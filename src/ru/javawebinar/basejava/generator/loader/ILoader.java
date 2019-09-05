package ru.javawebinar.basejava.generator.loader;

import java.util.ArrayList;
import java.util.List;

@FunctionalInterface
public interface ILoader<E> {
    /**
     * Load data from some source
     *
     * @return {@link List java.util.List&lt;T&gt;} List of values
     */
    List<E> load();

    /**
     * List of values which will be used instead of main data source
     * if it will be empty.
     *
     * @return {@link List java.util.List&lt;T&gt;} List of values
     */
    default List<E> defaultList() {
        return new ArrayList<>();
    }
}
