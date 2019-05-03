package ru.javawebinar.basejava.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListSection extends Section {
    private final List<String> items = new ArrayList<>();

    public ListSection(String... items) {
        Collections.addAll(this.items, items);
    }

    @Override
    void appendToHtmlStringBuilder(StringBuilder builder) {
        for (String item : items) {
            if (builder.length() != 0) {
                builder.append("\r\n");
            }
            builder.append("- ");
            builder.append(item);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ListSection that = (ListSection) o;

        return items.equals(that.items);

    }

    @Override
    public int hashCode() {
        return items.hashCode();
    }
}
