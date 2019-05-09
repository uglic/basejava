package ru.javawebinar.basejava.storage.strategy;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DataStreamStorageStrategy implements IOStorageStrategy {
    private final static String FIN_CONTACTS = "-c";
    private final static String FIN_SECTIONS = "-s";
    private final static String FIN_BULLETEDTEXTLIST = "-sb";
    private final static String FIN_ORGANIZATIONLIST = "-so";
    private final static String FIN_ORGANIZATION_HISTORY = "-soh";

    @Override
    public void write(Resume resume, OutputStream outputStream) throws IOException {
        try (DataOutputStream dataOutputStream = new DataOutputStream(outputStream)) {
            dataOutputStream.writeUTF(resume.getUuid());
            dataOutputStream.writeUTF(resume.getFullName());

            for (ContactTypes contactType : ContactTypes.values()) {
                Contact contact = resume.getContact(contactType);
                if (contact != null) {
                    dataOutputStream.writeUTF(contactType.name());
                    dataOutputStream.writeUTF(contact.getName());
                    dataOutputStream.writeUTF(contact.getUrl());
                }
            }
            dataOutputStream.writeUTF(FIN_CONTACTS);

            for (SectionTypes sectionType : SectionTypes.values()) {
                AbstractSection section = resume.getSection(sectionType);
                if (section != null) {
                    dataOutputStream.writeUTF(sectionType.name());
                    if (section.getClass() == SimpleTextSection.class) {
                        dataOutputStream.writeUTF(((SimpleTextSection) section).getContent());
                    } else if (section.getClass() == BulletedTextListSection.class) {
                        for (String item : ((BulletedTextListSection) section).getItems()) {
                            dataOutputStream.writeUTF(item);
                        }
                        dataOutputStream.writeUTF(FIN_BULLETEDTEXTLIST);
                    } else if (section.getClass() == OrganizationSection.class) {
                        for (Organization organization : ((OrganizationSection) section).getOrganizations()) {
                            dataOutputStream.writeUTF(organization.getContact().getName());
                            dataOutputStream.writeUTF(organization.getContact().getUrl());
                            for (Organization.Period history : organization.getHistory()) {
                                dataOutputStream.writeUTF(history.getStartDate().toString());
                                dataOutputStream.writeUTF(history.getEndDate().toString());
                                dataOutputStream.writeUTF(history.getTitle());
                                dataOutputStream.writeUTF(history.getDescription());
                            }
                            dataOutputStream.writeUTF(FIN_ORGANIZATION_HISTORY);
                        }
                        dataOutputStream.writeUTF(FIN_ORGANIZATIONLIST);
                    }
                }
            }
            dataOutputStream.writeUTF(FIN_SECTIONS);
        }
    }

    @Override
    public Resume read(InputStream inputStream) throws IOException {
        Resume resume = null;
        try (DataInputStream dataInputStream = new DataInputStream(inputStream)) {
            resume = new Resume(dataInputStream.readUTF(), dataInputStream.readUTF());
            String buffer;
            while (!(buffer = dataInputStream.readUTF()).equals(FIN_CONTACTS)) {
                ContactTypes contactType = ContactTypes.valueOf(buffer);
                Contact contact = new Contact(dataInputStream.readUTF(), dataInputStream.readUTF());
                resume.addContact(contactType, contact);
            }
            while (!(buffer = dataInputStream.readUTF()).equals(FIN_SECTIONS)) {
                SectionTypes sectionType = SectionTypes.valueOf(buffer);
                AbstractSection section;
                switch (sectionType) {
                    case OBJECTIVE:
                    case PERSONAL:
                        section = new SimpleTextSection(dataInputStream.readUTF());
                        break;
                    case ACHIEVEMENT:
                    case QUALIFICATIONS:
                        List<String> bulletedItems = new ArrayList<>();
                        while (!(buffer = dataInputStream.readUTF()).equals(FIN_BULLETEDTEXTLIST)) {
                            bulletedItems.add(buffer);
                        }
                        section = new BulletedTextListSection(bulletedItems);
                        break;
                    case EXPERIENCE:
                    case EDUCATION:
                        List<Organization> organizations = new ArrayList<>();
                        while (!(buffer = dataInputStream.readUTF()).equals(FIN_ORGANIZATIONLIST)) {
                            Contact contact = new Contact(buffer, dataInputStream.readUTF());
                            List<Organization.Period> history = new ArrayList<>();
                            while (!(buffer = dataInputStream.readUTF()).equals(FIN_ORGANIZATION_HISTORY)) {
                                Organization.Period period = new Organization.Period(
                                        LocalDate.parse(buffer),
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
