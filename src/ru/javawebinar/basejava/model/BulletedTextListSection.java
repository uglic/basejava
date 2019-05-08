package ru.javawebinar.basejava.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class BulletedTextListSection extends AbstractSection {
    private static final long serialVersionUID = 1L;

    private final List<String> items = new ArrayList<>();

    private BulletedTextListSection() { // only for marshalling
    }

    public BulletedTextListSection(String... items) {
        Objects.requireNonNull(items, "items must not be null");
        Collections.addAll(this.items, items);
    }

    public BulletedTextListSection(List<String> items) {
        Objects.requireNonNull(items, "items must not be null");
        this.items.addAll(items);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (String item : items) {
            if (builder.length() != 0) {
                builder.append("\r\n");
            }
            builder.append("- ");
            builder.append(item);
        }
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BulletedTextListSection that = (BulletedTextListSection) o;
        return items.equals(that.items);
    }

    @Override
    public int hashCode() {
        return items.hashCode();
    }
}
