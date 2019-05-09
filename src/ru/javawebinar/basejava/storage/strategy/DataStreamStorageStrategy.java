package ru.javawebinar.basejava.storage.strategy;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;

public class DataStreamStorageStrategy implements IOStorageStrategy {
    @Override
    public void write(Resume resume, OutputStream outputStream) throws IOException {
        try (DataOutputStream dataOutputStream = new DataOutputStream(outputStream)) {
            dataOutputStream.writeUTF(resume.getUuid());
            dataOutputStream.writeUTF(resume.getFullName());
            Map<ContactTypes, Contact> contacts = resume.getContacts();
            dataOutputStream.writeInt(contacts.size());

            //contacts.entrySet().stream().map(Map.Entry::getKey).map(ContactTypes::name).forEach(dataOutputStream::writeUTF);
            //contacts.entrySet().stream().map(Map.Entry::getValue).map(Contact::getName).forEach(dataOutputStream::writeUTF);
            //contacts.entrySet().stream().map(Map.Entry::getValue).map(Contact::getUrl).forEach(dataOutputStream::writeUTF);

            //DataStreamStorageStrategy.<Map.Entry<ContactTypes, Contact>>forEachWithIOException(contacts.entrySet(), dataOutputStream, (dataOutputStreamF, mapEntry) -> {
            //    dataOutputStreamF.writeUTF(mapEntry.getKey().name());
            //    dataOutputStreamF.writeUTF(mapEntry.getValue().getName());
            //    dataOutputStreamF.writeUTF(mapEntry.getValue().getUrl());
            //});

            forEachWriteWithIOException(contacts.entrySet(), dataOutputStream, mapEntry -> Arrays.asList(
                    mapEntry.getKey().name(),
                    mapEntry.getValue().getName(),
                    mapEntry.getValue().getUrl()
            ));

            Map<SectionTypes, AbstractSection> sections = resume.getSections();
            dataOutputStream.writeInt(sections.size());

            for (Map.Entry<SectionTypes, AbstractSection> abstractSectionEntry : sections.entrySet()) {
                AbstractSection section = abstractSectionEntry.getValue();
                dataOutputStream.writeUTF(abstractSectionEntry.getKey().name());
                switch (abstractSectionEntry.getKey()) {
                    case OBJECTIVE:
                    case PERSONAL:
                        dataOutputStream.writeUTF(((SimpleTextSection) section).getContent());
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        dataOutputStream.writeInt(((BulletedTextListSection) section).getItems().size());
                        for (String item : ((BulletedTextListSection) section).getItems()) {
                            dataOutputStream.writeUTF(item);
                        }
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        dataOutputStream.writeInt(((OrganizationSection) section).getOrganizations().size());
                        for (Organization organization : ((OrganizationSection) section).getOrganizations()) {
                            dataOutputStream.writeUTF(organization.getContact().getName());
                            dataOutputStream.writeUTF(organization.getContact().getUrl());
                            dataOutputStream.writeInt(organization.getHistory().size());
                            for (Organization.Position history : organization.getHistory()) {
                                dataOutputStream.writeUTF(history.getStartDate().toString());
                                dataOutputStream.writeUTF(history.getEndDate().toString());
                                dataOutputStream.writeUTF(history.getTitle());
                                dataOutputStream.writeUTF(history.getDescription());
                            }
                        }
                        break;
                    default:
                }
            }
        }
    }

    @Override
    public Resume read(InputStream inputStream) throws IOException {
        Resume resume = null;
        try (DataInputStream dataInputStream = new DataInputStream(inputStream)) {
            resume = new Resume(dataInputStream.readUTF(), dataInputStream.readUTF());
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

    private static <T> void forEachWriteWithIOException(Collection<? extends T> collection, DataOutputStream dataOutputStream, Function<? super T, List<String>> action) throws IOException {
        for (T t : collection) {
            for (String t2 : action.apply(t)) {
                dataOutputStream.writeUTF(t2);
            }
        }
    }

    private static <T> void forEachWriteWithIOException2(Collection<? extends T> collection, DataOutputStream dataOutputStream, Function<? super T, String> action) throws IOException {
        for (T t : collection) {
            dataOutputStream.writeUTF(action.apply(t));
        }
    }

}
