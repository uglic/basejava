package ru.javawebinar.basejava.model;

import java.util.Objects;

public enum ContactTypes {
    PHONE("Тел."),
    SKYPE("Skype"),
    EMAIL("Почта"),
    LINKEDIN("Профиль LinkedIn"),
    GITHUB("Профиль Github"),
    STACKOVERFLOW("Профиль Stackoverflow"),
    HOMESITE("Домашняя страница");

    private final String prefix;

    ContactTypes(String prefix) {
        Objects.requireNonNull(prefix);
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}