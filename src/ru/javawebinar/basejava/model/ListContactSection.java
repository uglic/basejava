package ru.javawebinar.basejava.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListContactSection extends Section {
    private final List<ListContactHistorySection> items = new ArrayList<>();

    public ListContactSection(ListContactHistorySection... items) {
        Collections.addAll(this.items, items);
    }

    @Override
    void appendToHtmlStringBuilder(StringBuilder builder) {
        for (ListContactHistorySection item : items) {
            if (builder.length() != 0) {
                builder.append("\r\n");
            }
            item.appendToHtmlStringBuilder(builder);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ListContactSection that = (ListContactSection) o;

        return items.equals(that.items);

    }

    @Override
    public int hashCode() {
        return items.hashCode();
    }
}
