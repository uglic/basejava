package ru.javawebinar.basejava.model;

public enum IconsEnum {
    NONE(""),
    SKYPE("skype"),
    EMAIL("email"),
    LINKEDIN("linkedin"),
    GITHUB("github"),
    STACKOVERFLOW("stackoverflow");

    private String filename;

    IconsEnum(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
}
