package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;

public class StreamPathStorage extends AbstractPathStorage {
    private StorageStream storageStream;

    public StreamPathStorage(Path directory) {
        super(directory);
    }

    public StreamPathStorage(Path directory, StorageStream storageStream) {
        super(directory);
        this.storageStream = storageStream;
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
