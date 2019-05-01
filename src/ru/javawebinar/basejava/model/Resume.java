package ru.javawebinar.basejava.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Resume implements Comparable<Resume>, Model {
    private final String uuid;
    private String fullName;

    //{need review
    private final List<Contact> contacts = new ArrayList<>();
    private final List<Section> sections = new ArrayList<>();
    //}need review

    public Resume(String fullName) {
        this(UUID.randomUUID().toString(), fullName);
    }

    public Resume(String uuid, String fullName) {
        Objects.requireNonNull(uuid, "uuid must not be null");
        Objects.requireNonNull(fullName, "fullName must not be null");
        this.uuid = uuid;
        this.fullName = fullName;
    }

    public String getUuid() {
        return uuid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Resume resume = (Resume) o;

        return uuid.equals(resume.uuid) && fullName.equals(resume.fullName);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode() << 2 + fullName.hashCode() % 7;
    }

    @Override
    public String toString() {
        return uuid + " (" + fullName + ")";
    }

    @Override
    public int compareTo(Resume o) {
        int compareToFullnameResult = fullName.compareTo(o.fullName);
        return compareToFullnameResult != 0 ? compareToFullnameResult : uuid.compareTo(o.uuid);
    }

    //{need review
    public void addContact(Contact contact) {
        contacts.add(contact);
    }

    public void addSection(Section section) {
        sections.add(section);
    }

    @Override
    public String getAsHtml() {
        StringBuilder builder = new StringBuilder();
        for (Contact contact : contacts) {
            if (builder.length() != 0) {
                builder.append("\r\n");
            }
            builder.append(contact.getAsHtml());
        }
        for (Section section : sections) {
            if (builder.length() != 0) {
                builder.append("\r\n");
            }
            builder.append(section.getAsHtml());
        }
        return builder.toString();
    }

    @Override
    public void save() {
        System.out.println("TODO: " + getClass().getName() + ".save()");
    }

    @Override
    public void read() {
        System.out.println("TODO: " + getClass().getName() + ".read()");
    }
    //}need review
}