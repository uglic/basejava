package ru.javawebinar.basejava.storage.strategy;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
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
            for (Map.Entry<ContactTypes, Contact> contactEntry : contacts.entrySet()) {
                dataOutputStream.writeUTF(contactEntry.getKey().name());
                dataOutputStream.writeUTF(contactEntry.getValue().getName());
                dataOutputStream.writeUTF(contactEntry.getValue().getUrl());
            }
            Map<SectionTypes, AbstractSection> sections = resume.getSections();
            dataOutputStream.writeInt(sections.size());
            for (Map.Entry<SectionTypes, AbstractSection> abstractSectionEntry : sections.entrySet()) {
                AbstractSection section = abstractSectionEntry.getValue();
                dataOutputStream.writeUTF(abstractSectionEntry.getKey().name());
                if (section.getClass() == SimpleTextSection.class) {
                    dataOutputStream.writeUTF(((SimpleTextSection) section).getContent());
                } else if (section.getClass() == BulletedTextListSection.class) {
                    dataOutputStream.writeInt(((BulletedTextListSection) section).getItems().size());
                    for (String item : ((BulletedTextListSection) section).getItems()) {
                        dataOutputStream.writeUTF(item);
                    }
                } else if (section.getClass() == OrganizationSection.class) {
                    dataOutputStream.writeInt(((OrganizationSection) section).getOrganizations().size());
                    for (Organization organization : ((OrganizationSection) section).getOrganizations()) {
                        dataOutputStream.writeUTF(organization.getContact().getName());
                        dataOutputStream.writeUTF(organization.getContact().getUrl());
                        dataOutputStream.writeInt(organization.getHistory().size());
                        for (Organization.Period history : organization.getHistory()) {
                            dataOutputStream.writeUTF(history.getStartDate().toString());
                            dataOutputStream.writeUTF(history.getEndDate().toString());
                            dataOutputStream.writeUTF(history.getTitle());
                            dataOutputStream.writeUTF(history.getDescription());
                        }
                    }
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
                            List<Organization.Period> history = new ArrayList<>();
                            int historySize = dataInputStream.readInt();
                            for (int historyIndex = 0; historyIndex < historySize; historyIndex++) {
                                Organization.Period period = new Organization.Period(
                                        LocalDate.parse(dataInputStream.readUTF()),
                                        LocalDate.parse(dataInputStream.readUTF()),
                                        dataInputStream.readUTF(),
                                        dataInputStream.readUTF()
                                );
                                history.add(period);
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
        } catch (EOFException e) {
        } catch (IOException e) {
            throw new StorageException("Error read resume", e);
        }
        return resume;
    }
}
