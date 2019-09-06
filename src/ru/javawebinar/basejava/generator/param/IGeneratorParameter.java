package ru.javawebinar.basejava.generator.param;

import ru.javawebinar.basejava.model.ContactTypes;
import ru.javawebinar.basejava.model.SectionTypes;

import java.time.LocalDate;

public interface IGeneratorParameter {
    Boolean isMan();

    default ContactTypes getContactType() {
        throw new IllegalStateException();
    }

    default SectionTypes getSectionType() {
        throw new IllegalStateException();
    }

    default LocalDate getDateFrom() {
        throw new IllegalStateException();
    }

    default LocalDate getDateTo() {
        throw new IllegalStateException();
    }

    default String getFullName() {
        throw new IllegalStateException();
    }

    default String getLogin() {
        throw new IllegalStateException();
    }
}
