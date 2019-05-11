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
            forEachConsume(Arrays.asList(contacts.size()), size -> dataOutputStream.writeInt(size));
            forEachConsume(contacts.entrySet(), contactEntry -> {
                dataOutputStream.writeUTF(contactEntry.getKey().name());
                dataOutputStream.writeUTF(contactEntry.getValue().getName());
                dataOutputStream.writeUTF(contactEntry.getValue().getUrl());
            });
            Map<SectionTypes, AbstractSection> sections = resume.getSections();
            forEachConsume(Arrays.asList(sections.size()), size -> dataOutputStream.writeInt(size));
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
                        forEachConsume(Arrays.asList(((BulletedTextListSection) section).getItems().size()), size -> dataOutputStream.writeInt(size));
                        forEachConsume(((BulletedTextListSection) section).getItems(), dataOutputStream::writeUTF);
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        forEachConsume(Arrays.asList(((OrganizationSection) section).getOrganizations().size()), size -> dataOutputStream.writeInt(size));
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
            int contactsSize = dataInputStream.readInt();
            for (int contactIndex = 0; contactIndex < contactsSize; contactIndex++) {
                ContactTypes contactType = ContactTypes.valueOf(dataInputStream.readUTF());
                Contact contact = new Contact(dataInputStream.readUTF(), dataInputStream.readUTF());
                resume.addContact(contactType, contact);
            }
            int sectionsSize = dataInputStream.readInt();
            for (int sectionIndex = 0; sectionIndex < sectionsSize; sectionIndex++) {
                SectionTypes sectionType = SectionTypes.valueOf(dataInputStream.readUTF());
                AbstractSection section;
                switch (sectionType) {
                    case OBJECTIVE:
                    case PERSONAL:
                        section = new SimpleTextSection(dataInputStream.readUTF());
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        List<String> bulletedItems = new ArrayList<>();
                        int bulletedItemsSize = dataInputStream.readInt();
                        for (int bulletedItemIndex = 0; bulletedItemIndex < bulletedItemsSize; bulletedItemIndex++) {
                            bulletedItems.add(dataInputStream.readUTF());
                        }
                        section = new BulletedTextListSection(bulletedItems);
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        List<Organization> organizations = getListIOException(
                                ArrayList::new,
                                () -> new Organization(
                                        getListIOException(ArrayList::new, () -> new Contact(dataInputStream.readUTF(), dataInputStream.readUTF()), 1).get(0),
                                        getListIOException(
                                                ArrayList::new,
                                                () -> new Organization.Position(
                                                        LocalDate.parse(dataInputStream.readUTF()),
                                                        LocalDate.parse(dataInputStream.readUTF()),
                                                        dataInputStream.readUTF(),
                                                        dataInputStream.readUTF()),
                                                getListIOException(ArrayList::new, dataInputStream::readInt, 1).get(0))
                                ),
                                getListIOException(ArrayList::new, dataInputStream::readInt, 1).get(0));


//
//
//
//
//
//
//
//
//                        //----------
//                        int organizationsSize = dataInputStream.readInt();
//                        List<Organization> organizations = new ArrayList<>();
//                        for (int organizationIndex = 0; organizationIndex < organizationsSize; organizationIndex++) {
//                            Contact contact = new Contact(dataInputStream.readUTF(), dataInputStream.readUTF());
//                            organizations.add(
//                                    new Organization(
//                                            contact,
//                                            getListIOException(
//                                                    ArrayList::new,
//                                                    () -> new Organization.Position(
//                                                            LocalDate.parse(dataInputStream.readUTF()),
//                                                            LocalDate.parse(dataInputStream.readUTF()),
//                                                            dataInputStream.readUTF(),
//                                                            dataInputStream.readUTF()),
//                                                    getListIOException(ArrayList::new, dataInputStream::readInt, 1).get(0))
//                                    )
//                            );
//                        }
                        section = new OrganizationSection(organizations);
                        break;
                    default:
                        throw new StorageException("Unknown section: " + sectionType);
                }
                resume.addSection(sectionType, section);
            }
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
}
