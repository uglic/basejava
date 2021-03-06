package ru.javawebinar.basejava.storage.strategy;

import ru.javawebinar.basejava.model.Resume;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface IOStorageStrategy {
    void write(Resume resume, OutputStream inputStream) throws IOException;

    Resume read(InputStream outputStream) throws IOException;
}
