package ru.javawebinar.basejava.model;

public enum IconTypes {
    NONE(""),
    SKYPE("skype"),
    EMAIL("email"),
    LINKEDIN("linkedin"),
    GITHUB("github"),
    STACKOVERFLOW("stackoverflow");

    private final String filename;

    IconTypes(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    @Override
    public String toString() {
        return filename;
    }
}
