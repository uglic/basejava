package ru.javawebinar.basejava.model;

public enum SectionsEnum {
    PERSONAL("Личные качества"),
    OBJECTIVE("Позиция"),
    ACHIEVEMENT("Достижения"),
    QUALIFICATIONS("Квалификация"),
    EXPERIENCE("Опыт работы"),
    EDUCATION("Образование");

    private String title;

    SectionsEnum(String title) {
        this.title = title;
    }

    public String getTitle(){
        return title;
    }
}
