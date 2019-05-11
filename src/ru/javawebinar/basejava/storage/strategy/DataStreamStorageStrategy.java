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
            dataOutputStream.writeInt(contacts.size());
            forEachDataOutputStream(contacts.entrySet(), dataOutputStream, (stream, entry) -> {
                stream.writeUTF(entry.getKey().name());
                stream.writeUTF(entry.getValue().getName());
                stream.writeUTF(entry.getValue().getUrl());
            });
            Map<SectionTypes, AbstractSection> sections = resume.getSections();
            dataOutputStream.writeInt(sections.size());
            forEachDataOutputStream(sections.entrySet(), dataOutputStream, (stream, abstractSectionEntry) -> {
                AbstractSection section = abstractSectionEntry.getValue();
                stream.writeUTF(abstractSectionEntry.getKey().name());
                switch (abstractSectionEntry.getKey()) {
                    case OBJECTIVE:
                    case PERSONAL:
                        stream.writeUTF(((SimpleTextSection) section).getContent());
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        stream.writeInt(((BulletedTextListSection) section).getItems().size());
                        forEachDataOutputStream(((BulletedTextListSection) section).getItems(), stream, DataOutputStream::writeUTF);
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        stream.writeInt(((OrganizationSection) section).getOrganizations().size());
                        forEachDataOutputStream(((OrganizationSection) section).getOrganizations(), stream, (streamOrg, organization) -> {
                            streamOrg.writeUTF(organization.getContact().getName());
                            streamOrg.writeUTF(organization.getContact().getUrl());
                            streamOrg.writeInt(organization.getHistory().size());
                            forEachDataOutputStream(organization.getHistory(), streamOrg, (streamHistory, history) -> {
                                streamHistory.writeUTF(history.getStartDate().toString());
                                streamHistory.writeUTF(history.getEndDate().toString());
                                streamHistory.writeUTF(history.getTitle());
                                streamHistory.writeUTF(history.getDescription());
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
                        List<Organization> organizations = new ArrayList<>();
                        int organizationsSize = dataInputStream.readInt();
                        for (int organizationIndex = 0; organizationIndex < organizationsSize; organizationIndex++) {
                            Contact contact = new Contact(dataInputStream.readUTF(), dataInputStream.readUTF());
                            List<Organization.Position> history = new ArrayList<>();
                            int historySize = dataInputStream.readInt();
                            for (int historyIndex = 0; historyIndex < historySize; historyIndex++) {
                                Organization.Position position = new Organization.Position(
                                        LocalDate.parse(dataInputStream.readUTF()),
                                        LocalDate.parse(dataInputStream.readUTF()),
                                        dataInputStream.readUTF(),
                                        dataInputStream.readUTF()
                                );
                                history.add(position);
                            }
                            organizations.add(new Organization(contact, history));
                        }
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
    private interface DataOutputBiConsumer<S extends DataOutputStream, T> {
        void apply(S dataOutputStream, T t) throws IOException;
    }

    private static <S extends DataOutputStream, T> void forEachDataOutputStream(Collection<? extends T> collection, S dataOutputStream, DataOutputBiConsumer<S, T> action) throws IOException {
        for (T t : collection) {
            action.apply(dataOutputStream, t);
        }
    }
}
