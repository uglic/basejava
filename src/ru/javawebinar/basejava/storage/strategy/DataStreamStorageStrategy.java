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
            forEachConsume(Arrays.asList(contacts.size()), dataOutputStream::writeInt);
            forEachConsume(contacts.entrySet(), contactEntry -> {
                dataOutputStream.writeUTF(contactEntry.getKey().name());
                dataOutputStream.writeUTF(contactEntry.getValue().getName());
                dataOutputStream.writeUTF(contactEntry.getValue().getUrl());
            });
            Map<SectionTypes, AbstractSection> sections = resume.getSections();
            forEachConsume(Arrays.asList(sections.size()), dataOutputStream::writeInt);
            forEachConsume(sections.entrySet(), abstractSectionEntry -> {
                AbstractSection section = abstractSectionEntry.getValue();
                dataOutputStream.writeUTF(abstractSectionEntry.getKey().name());
                switch (abstractSectionEntry.getKey()) {
                    case OBJECTIVE:
                    case PERSONAL:
                        dataOutputStream.writeUTF(((SimpleTextSection) section).getContent());
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        forEachConsume(Arrays.asList(((BulletedTextListSection) section).getItems().size()), dataOutputStream::writeInt);
                        forEachConsume(((BulletedTextListSection) section).getItems(), dataOutputStream::writeUTF);
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        forEachConsume(Arrays.asList(((OrganizationSection) section).getOrganizations().size()), dataOutputStream::writeInt);
                        forEachConsume(((OrganizationSection) section).getOrganizations(), organization -> {
                            dataOutputStream.writeUTF(organization.getContact().getName());
                            dataOutputStream.writeUTF(organization.getContact().getUrl());
                            dataOutputStream.writeInt(organization.getHistory().size());
                            forEachConsume(organization.getHistory(), history -> {
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

            addContacts(resume,
                    () -> ContactTypes.valueOf(dataInputStream.readUTF()),
                    () -> new Contact(dataInputStream.readUTF(), dataInputStream.readUTF()),
                    getListIOException(ArrayList::new, dataInputStream::readInt, 1).get(0));

            addSections(resume,
                    () -> SectionTypes.valueOf(dataInputStream.readUTF()),
                    (sectionType) -> {
                        switch (sectionType) {
                            case OBJECTIVE:
                            case PERSONAL:
                                return new SimpleTextSection(
                                        getListIOException(ArrayList::new,
                                                dataInputStream::readUTF, 1).get(0));
                            case ACHIEVEMENT:
                            case QUALIFICATIONS:
                                return new BulletedTextListSection(
                                        getListIOException(ArrayList::new,
                                                dataInputStream::readUTF,
                                                getListIOException(ArrayList::new,
                                                        dataInputStream::readInt, 1).get(0)));
                            case EXPERIENCE:
                            case EDUCATION:
                                return new OrganizationSection(
                                        getListIOException(ArrayList::new,
                                                () -> new Organization(
                                                        getListIOException(ArrayList::new,
                                                                () -> new Contact(dataInputStream.readUTF(), dataInputStream.readUTF()), 1).get(0),
                                                        getListIOException(ArrayList::new,
                                                                () -> new Organization.Position(
                                                                        LocalDate.parse(dataInputStream.readUTF()),
                                                                        LocalDate.parse(dataInputStream.readUTF()),
                                                                        dataInputStream.readUTF(),
                                                                        dataInputStream.readUTF()),
                                                                getListIOException(ArrayList::new,
                                                                        dataInputStream::readInt, 1).get(0))
                                                ),
                                                getListIOException(ArrayList::new, dataInputStream::readInt, 1).get(0)));
                            default:
                                throw new StorageException("Unknown section: " + sectionType);
                        }
                    },
                    getListIOException(ArrayList::new, dataInputStream::readInt, 1).get(0));
            return resume;
        }
    }

    @FunctionalInterface
    private interface IOExceptionConsumer<T> {
        void apply(T t) throws IOException;
    }

    @FunctionalInterface
    private interface IOExceptionSupplier<T> {
        T get() throws IOException;
    }

    @FunctionalInterface
    private interface IOExceptionBiSupplier<T, V> {
        T get(V v) throws IOException;
    }

    private static <T> void forEachConsume(Collection<? extends T> collection, IOExceptionConsumer<T> action) throws IOException {
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

    private static void addContacts(Resume resume, IOExceptionSupplier<ContactTypes> contactType, IOExceptionSupplier<Contact> contact, int size) throws IOException {
        for (int i = 0; i < size; i++) {
            resume.addContact(contactType.get(), contact.get());
        }
    }

    private static void addSections(Resume resume, IOExceptionSupplier<SectionTypes> sectionType, IOExceptionBiSupplier<AbstractSection, SectionTypes> section, int size) throws IOException {
        for (int i = 0; i < size; i++) {
            SectionTypes type = sectionType.get();
            resume.addSection(type, section.get(type));
        }
    }
}
