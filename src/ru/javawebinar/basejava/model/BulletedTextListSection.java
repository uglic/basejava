package ru.javawebinar.basejava.model;

import java.util.*;

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

    public List<String> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return items.toString();
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

    public static AbstractSection getEmpty(){
        return new BulletedTextListSection("");
    }
}
