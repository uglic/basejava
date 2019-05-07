package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;

public abstract class PathStreamStorage extends AbstractPathStorage {
    private StorageStream storageStream;

    protected PathStreamStorage(Path directory) {
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
