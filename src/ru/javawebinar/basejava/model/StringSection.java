package ru.javawebinar.basejava.model;

import java.util.Objects;

public class StringSection extends Section {
    private final String content;

    public StringSection(String content) {
        Objects.requireNonNull(content, "Section content must be non-null");
        this.content = content;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (content != null && !content.isEmpty()) {
            builder.append(content);
        }
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StringSection that = (StringSection) o;
        return content.equals(that.content);
    }

    @Override
    public int hashCode() {
        return content.hashCode();
    }
}
