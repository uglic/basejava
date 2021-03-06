package ru.javawebinar.basejava.storage.strategy;

import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.util.XmlParser;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class XmlStorageStrategy implements IOStorageStrategy {
    private final XmlParser xmlParser;

    public XmlStorageStrategy() {
        xmlParser = new XmlParser(
                Resume.class, Contact.class,
                BulletedTextListSection.class, OrganizationSection.class, SimpleTextSection.class,
                Contact.class, Organization.class, Organization.Position.class);
    }

    @Override
    public void write(Resume resume, OutputStream outputStream) throws IOException {
        try (Writer writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {
            xmlParser.marshall(resume, writer);
        }
    }

    @Override
    public Resume read(InputStream inputStream) throws IOException {
        try (Reader reader = new InputStreamReader(inputStream)) {
            return (Resume) xmlParser.unmarshall(reader);
        }
    }
}
