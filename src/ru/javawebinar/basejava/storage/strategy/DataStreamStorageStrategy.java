package ru.javawebinar.basejava.storage.strategy;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Supplier;

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

            addElements(resume::addContact,
                    () -> ContactTypes.valueOf(dataInputStream.readUTF()),
                    (type) -> new Contact(dataInputStream.readUTF(), dataInputStream.readUTF()),
                    dataInputStream.readInt());

            addElements(resume::addSection,
                    () -> SectionTypes.valueOf(dataInputStream.readUTF()),
                    (sectionType) -> {
                        switch (sectionType) {
                            case OBJECTIVE:
                            case PERSONAL:
                                return new SimpleTextSection(dataInputStream.readUTF());
                            case ACHIEVEMENT:
                            case QUALIFICATIONS:
                                return new BulletedTextListSection(
                                        getListIOException(ArrayList::new,
                                                dataInputStream::readUTF,
                                                dataInputStream.readInt()));
                            case EXPERIENCE:
                            case EDUCATION:
                                return new OrganizationSection(
                                        getListIOException(ArrayList::new,
                                                () -> new Organization(
                                                        new Contact(dataInputStream.readUTF(), dataInputStream.readUTF()),
                                                        getListIOException(ArrayList::new,
                                                                () -> new Organization.Position(
                                                                        LocalDate.parse(dataInputStream.readUTF()),
                                                                        LocalDate.parse(dataInputStream.readUTF()),
                                                                        dataInputStream.readUTF(),
                                                                        dataInputStream.readUTF()),
                                                                dataInputStream.readInt())
                                                ),
                                                dataInputStream.readInt()));
                            default:
                                throw new StorageException("Unknown section: " + sectionType);
                        }
                    },
                    dataInputStream.readInt());
            return resume;
        }
    }

    @FunctionalInterface
    private interface IOExceptionConsumer<T> {
        void apply(T t) throws IOException;
    }

    @FunctionalInterface
    private interface IOExceptionBiConsumer<T, U> {
        void apply(T t, U u) throws IOException;
    }

    @FunctionalInterface
    private interface IOExceptionSupplier<T> {
        T get() throws IOException;
    }

    @FunctionalInterface
    private interface IOExceptionBiSupplier<T, V> {
        T get(V v) throws IOException;
    }

    private static <T> void forEachConsume(Collection<? extends T> collection, DataOutputStream dataOutputStream, IOExceptionConsumer<T> action) throws IOException {
        dataOutputStream.writeInt(collection.size());
        for (T t : collection) {
            action.apply(t);
        }
    }

    private static <T> List<T> getListIOException(Supplier<? extends List<T>> collectionSupplier, IOExceptionSupplier<T> element, int size) throws IOException {
        List<T> collection = collectionSupplier.get();
        for (int i = 0; i < size; i++) {
            collection.add(element.get());
        }
        return collection;
    }

    private static <ET, T, U> void addElements(IOExceptionBiConsumer<ET, T> action, IOExceptionSupplier<ET> elementType, IOExceptionBiSupplier<T, ET> element, int size) throws IOException {
        for (int i = 0; i < size; i++) {
            ET type = elementType.get();
            action.apply(type, element.get(type));
        }
    }
}
