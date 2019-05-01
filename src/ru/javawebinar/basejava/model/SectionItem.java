package ru.javawebinar.basejava.model;

public abstract class SectionItem implements Model {
    private String title;

    protected final void setTitle(String title) {
        this.title = title;
    }

    protected final String getTitle() {
        return title;
    }
}
