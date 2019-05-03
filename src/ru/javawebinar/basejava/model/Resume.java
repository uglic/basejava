package ru.javawebinar.basejava.model;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Resume implements Comparable<Resume> {
    private final String uuid;
    private String fullName;

    private final Map<ContactTypes, Contact> contacts = new EnumMap<>(ContactTypes.class);
    private final Map<SectionTypes, AbstractSection> sections = new EnumMap<>(SectionTypes.class);

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

    public void addSection(SectionTypes type, AbstractSection section) {
        sections.put(type, section);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(uuid);
        builder.append(" (");
        builder.append(fullName);
        builder.append(")");
        for (ContactTypes contactType : ContactTypes.values()) {
            Contact contact = contacts.get(contactType);
            if (contact != null) {
                if (builder.length() != 0) {
                    builder.append("\r\n");
                }
                builder.append(contactType);
                builder.append(contact);
            }
        }
        for (SectionTypes sectionType : SectionTypes.values()) {
            AbstractSection section = sections.get(sectionType);
            if (section != null) {
                if (builder.length() != 0) {
                    builder.append("\r\n\r\n");
                }
                builder.append("# ");
                builder.append(sectionType);
                builder.append("\r\n");
                builder.append(section);
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