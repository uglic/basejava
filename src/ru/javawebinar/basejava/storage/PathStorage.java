package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.strategy.IOStorageStrategy;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PathStorage extends AbstractStorage<Path> {
    private Path directory;
    private IOStorageStrategy storageStrategy;

    public PathStorage(Path directory, IOStorageStrategy storageStrategy) {
        Objects.requireNonNull(directory, "directory must not be null");
        if (!Files.isDirectory(directory)) {
            throw new IllegalArgumentException(directory + " is not directory");
        }
        if (!Files.isReadable(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException(directory + " is not readable/writable");
        }
        this.directory = directory;
        Objects.requireNonNull(storageStrategy, "storageStrategy must not be null");
        this.storageStrategy = storageStrategy;
    }

    public void setStorageStrategy(IOStorageStrategy storageStrategy) {
        this.storageStrategy = storageStrategy;
    }

    @Override
    protected Path getSearchKey(String uuid) {
        return directory.resolve(uuid);
    }

    @Override
    protected Resume doGet(Path path) {
        try {
            return storageStrategy.read(new BufferedInputStream(new FileInputStream(path.toFile())));
        } catch (IOException e) {
            throw new StorageException("IO error", path.getFileName().toString(), e);
        }
    }

    @Override
    protected void doUpdate(Resume resume, Path path) {
        try {
            storageStrategy.write(resume, new BufferedOutputStream(new FileOutputStream(path.toFile())));
        } catch (IOException e) {
            throw new StorageException("IO error", path.getFileName().toString(), e);
        }
    }

    @Override
    protected void doSave(Resume resume, Path path) {
        try {
            Files.createFile(path);
            storageStrategy.write(resume, new BufferedOutputStream(new FileOutputStream(path.toFile())));
        } catch (IOException e) {
            throw new StorageException("IO error", path.getFileName().toString(), e);
        }
    }

    @Override
    protected void doDelete(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new StorageException("Delete error", path.getFileName().toString(), e);
        }
    }

    @Override
    protected boolean isExist(Path path) {
        return Files.isRegularFile(path);
    }

    @Override
    protected List<Resume> doCopyAll() {
        try {
            return Files.list(directory)
                    .filter(((Predicate<Path>) Files::isDirectory).negate())
                    .map(this::doGet)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new StorageException(directory + " directory read error", e);
        }
    }

    @Override
    public int size() {
        try {
            return (int) Files.list(directory)
                    .filter(((Predicate<Path>) Files::isDirectory).negate())
                    .count();
        } catch (IOException e) {
            throw new StorageException(directory + " directory read error", e);
        }
    }

    @Override
    public void clear() {
        try {
            Files.list(directory)
                    .filter(((Predicate<Path>) Files::isDirectory).negate())
                    .forEach(this::doDelete);
        } catch (IOException e) {
            throw new StorageException(directory + " directory clear error", e);
        }
    }
}
