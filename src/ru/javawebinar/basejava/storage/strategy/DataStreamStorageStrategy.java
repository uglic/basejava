package ru.javawebinar.basejava.storage.strategy;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class DataStreamStorageStrategy implements IOStorageStrategy {
    @Override
    public void write(Resume resume, OutputStream outputStream) throws IOException {
        try (DataOutputStream dataOutputStream = new DataOutputStream(outputStream)) {
            dataOutputStream.writeUTF(resume.getUuid());
            dataOutputStream.writeUTF(resume.getFullName());
            Map<ContactTypes, Contact> contacts = resume.getContacts();
            forEachConsume(contacts.entrySet(), dataOutputStream, contactEntry -> {
                dataOutputStream.writeUTF(contactEntry.getKey().name());
                dataOutputStream.writeUTF(contactEntry.getValue().getName());
                dataOutputStream.writeUTF(contactEntry.getValue().getUrl());
            });
            Map<SectionTypes, AbstractSection> sections = resume.getSections();
            forEachConsume(sections.entrySet(), dataOutputStream, abstractSectionEntry -> {
                AbstractSection section = abstractSectionEntry.getValue();
                dataOutputStream.writeUTF(abstractSectionEntry.getKey().name());
                switch (abstractSectionEntry.getKey()) {
                    case OBJECTIVE:
                    case PERSONAL:
                        dataOutputStream.writeUTF(((SimpleTextSection) section).getContent());
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        forEachConsume(((BulletedTextListSection) section).getItems(), dataOutputStream, dataOutputStream::writeUTF);
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        forEachConsume(((OrganizationSection) section).getOrganizations(), dataOutputStream, organization -> {
                            dataOutputStream.writeUTF(organization.getContact().getName());
                            dataOutputStream.writeUTF(organization.getContact().getUrl());
                            forEachConsume(organization.getHistory(), dataOutputStream, history -> {
                                dataOutputStream.writeUTF(history.getStartDate().toString());
                                dataOutputStream.writeUTF(history.getEndDate().toString());
                                dataOutputStream.writeUTF(history.getTitle());
                                dataOutputStream.writeUTF(history.getDescription());
                            });
                        });
                        break;
                    default:
                }
            });
        }
    }

    @Override
    public Resume read(InputStream inputStream) throws IOException {
        try (DataInputStream dataInputStream = new DataInputStream(inputStream)) {
            Resume resume = new Resume(dataInputStream.readUTF(), dataInputStream.readUTF());

            addElements(dataInputStream, () -> {
                ContactTypes type = ContactTypes.valueOf(dataInputStream.readUTF());
                resume.addContact(type, new Contact(dataInputStream.readUTF(), dataInputStream.readUTF()));
            });

            addElements(dataInputStream, () -> {
                        SectionTypes sectionType = SectionTypes.valueOf(dataInputStream.readUTF());
                        AbstractSection section;
                        switch (sectionType) {
                            case OBJECTIVE:
                            case PERSONAL:
                                section = new SimpleTextSection(dataInputStream.readUTF());
                                break;
                            case ACHIEVEMENT:
                            case QUALIFICATIONS:
                                section = new BulletedTextListSection(
                                        getListIOException(dataInputStream::readUTF, dataInputStream));
                                break;
                            case EXPERIENCE:
                            case EDUCATION:
                                section = new OrganizationSection(
                                        getListIOException(() -> new Organization(
                                                new Contact(dataInputStream.readUTF(), dataInputStream.readUTF()),
                                                getListIOException(() -> new Organization.Position(
                                                                LocalDate.parse(dataInputStream.readUTF()),
                                                                LocalDate.parse(dataInputStream.readUTF()),
                                                                dataInputStream.readUTF(),
                                                                dataInputStream.readUTF()),
                                                        dataInputStream)
                                        ), dataInputStream));
                                break;
                            default:
                                throw new StorageException("Unknown section: " + sectionType);
                        }
                        resume.addSection(sectionType, section);
                    }
            );
            return resume;
        }
    }

    @FunctionalInterface
    private interface IOExceptionUnary {
        void apply() throws IOException;
    }

    @FunctionalInterface
    private interface IOExceptionConsumer<T> {
        void accept(T t) throws IOException;
    }

    @FunctionalInterface
    private interface IOExceptionSupplier<T> {
        T get() throws IOException;
    }

    private <T> void forEachConsume(Collection<? extends T> collection, DataOutputStream dataOutputStream, IOExceptionConsumer<T> action) throws IOException {
        dataOutputStream.writeInt(collection.size());
        for (T t : collection) {
            action.accept(t);
        }
    }

    private <T> List<T> getListIOException(IOExceptionSupplier<T> element, DataInputStream dataInputStream) throws IOException {
        int size = dataInputStream.readInt();
        List<T> collection = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            collection.add(element.get());
        }
        return collection;
    }

    private void addElements(DataInputStream dataInputStream, IOExceptionUnary action) throws IOException {
        int size = dataInputStream.readInt();
        for (int i = 0; i < size; i++) {
            action.apply();
        }
    }
}
