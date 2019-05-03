package ru.javawebinar.basejava.model;

import java.util.Objects;

public enum SectionTypes {
    OBJECTIVE("Позиция"),
    PERSONAL("Личные качества"),
    ACHIEVEMENT("Достижения"),
    QUALIFICATIONS("Квалификация"),
    EXPERIENCE("Опыт работы"),
    EDUCATION("Образование");

    private final String title;

    SectionTypes(String title) {
        Objects.requireNonNull(title);
        this.title = title;
    }

    @Override
    public String toString(){
        return title;
    }
}
