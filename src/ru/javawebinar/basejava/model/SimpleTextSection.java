package ru.javawebinar.basejava.model;

import java.util.Objects;

public class SimpleTextSection extends AbstractSection {
    private static final long serialVersionUID = 1L;

    private final String content;

    private SimpleTextSection() { // only for marshalling
        this("");
    }

    public SimpleTextSection(String content) {
        Objects.requireNonNull(content, "Section content must be non-null");
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleTextSection that = (SimpleTextSection) o;
        return content.equals(that.content);
    }

    @Override
    public int hashCode() {
        return content.hashCode();
    }
}
