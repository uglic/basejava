package ru.javawebinar.basejava.model;

import java.util.Comparator;
import java.util.UUID;

/**
 * ru.javawebinar.basejava.model.Resume class
 */
public class Resume implements Comparable<Resume> {
    public static final Comparator<Resume> RESUME_COMPARATOR = (o1, o2) -> {
        int compareNames = o1.getFullName().compareTo(o2.getFullName());
        if (compareNames == 0) {
            return o1.getUuid().compareTo(o2.getUuid());
        } else {
            return compareNames;
        }
    };

    // Unique identifier
    private final String uuid;

    private String fullName;

    public Resume() {
        this(UUID.randomUUID().toString(), "");
    }

    public Resume(String uuid, String fullName) {
        if ((uuid == null) || (fullName == null)) throw new NullPointerException();
        this.uuid = uuid;
        this.fullName = fullName;
    }

    public Resume(Resume resume) {
        this.uuid = resume.uuid;
        this.fullName = resume.fullName;
    }

    public String getUuid() {
        return uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Resume resume = (Resume) o;

        return uuid.equals(resume.uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    @Override
    public String toString() {
        return uuid;
    }

    // About this method homework for lesson#06 says nothing
    @Override
    public int compareTo(Resume o) {
        return uuid.compareTo(o.uuid);
    }

    public String getFullName() {
        return fullName;
    }
}