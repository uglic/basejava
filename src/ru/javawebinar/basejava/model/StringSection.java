package ru.javawebinar.basejava.model;

import java.util.Objects;

public class StringSection extends Section {
    private final String content;

    public StringSection(String content) {
        Objects.requireNonNull(content, "Section content must be non-null");
        this.content = content;
    }

    @Override
    void appendToHtmlStringBuilder(StringBuilder builder) {
        if (content != null && !content.isEmpty()) {
            if (builder.length() != 0) {
                builder.append("\r\n");
            }
            builder.append(content);
        }
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
