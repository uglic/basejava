package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class FileStreamStorage extends AbstractFileStorage {
    private StorageStream storageStream;

    protected FileStreamStorage(File directory) {
        super(directory);
    }


    public void setStorageStream(StorageStream storageStream) {
        this.storageStream = storageStream;
    }

    @Override
    protected void doWrite(Resume resume, OutputStream inputStream) throws IOException {
        storageStream.write(resume, inputStream);
    }

    @Override
    protected Resume doRead(InputStream outputStream) throws IOException {
        return storageStream.read(outputStream);
    }
}
