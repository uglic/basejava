package ru.javawebinar.basejava.model;

import java.util.*;

public class Resume implements Comparable<Resume> {
    private final String uuid;
    private String fullName;

    private final Map<ContactTypes, Contact> contacts = new HashMap<>();
    private final Map<SectionTypes, Section> sections = new HashMap<>();

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

    public void addContact(ContactTypes type, Contact contact) {
        contacts.put(type, contact);
    }

    public void addSection(SectionTypes type, Section section) {
        sections.put(type, section);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (ContactTypes contactType : ContactTypes.values()) {
            Contact contact = contacts.get(contactType);
            if (contact != null) {
                if (builder.length() != 0) {
                    builder.append("\r\n");
                }
                builder.append(contactType);
                builder.append(contact.toString());
            }
        }
        for (SectionTypes sectionType : SectionTypes.values()) {
            Section section = sections.get(sectionType);
            if (section != null) {
                if (builder.length() != 0) {
                    builder.append("\r\n\r\n");
                }
                builder.append("# ");
                builder.append(sectionType);
                section.appendToHtmlStringBuilder(builder);
            }
        }
        return builder.toString();
    }

    @Override
    public int compareTo(Resume o) {
        int compareToFullnameResult = fullName.compareTo(o.fullName);
        return compareToFullnameResult != 0 ? compareToFullnameResult : uuid.compareTo(o.uuid);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Resume resume = (Resume) o;

        return (uuid.equals(resume.uuid))
                && fullName.equals(resume.fullName)
                && contacts.equals(resume.contacts)
                && sections.equals(resume.sections);

    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, fullName, contacts, sections);
    }
}