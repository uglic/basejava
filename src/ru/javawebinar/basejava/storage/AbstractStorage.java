package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractStorage implements Storage {

    @Override
    public void update(Resume resume) {
        int index = getIndex(resume.getUuid());
        if (index < 0) {
            throw new NotExistStorageException(resume.toString());
        } else {
            setStorageElement(index, resume);
        }
    }

    @Override
    public void save(Resume resume) {
        int index = getIndex(resume.getUuid());
        if (index >= 0) {
            throw new ExistStorageException(resume.toString());
        } else {
            onBeforeSave(resume, index);
            insertElement(resume, index);
            onAfterSave(index);
        }
    }

    @Override
    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index < 0) {
            throw new NotExistStorageException(uuid);
        } else {
            onBeforeDelete(index);
            fillDeletedElement(index);
            onAfterDelete(index);
        }
    }

    @Override
    public Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index < 0) {
            throw new NotExistStorageException(uuid);
        }
        return getStorageElement(index);
    }

    protected abstract void fillDeletedElement(int index);

    protected abstract void insertElement(Resume resume, int index);

    protected abstract int getIndex(String uuid);

    /* added for part 1 of lesson 5 homework */
    protected abstract Resume getStorageElement(int index);

    /* added for part 1 of lesson 5 homework */
    protected abstract void setStorageElement(int index, Resume resume);

    /* added for part 1 of lesson 5 homework new entity */
    protected void onBeforeSave(Resume resume, int index) {
    }

    /* added for part 1 of lesson 5 homework new entity */
    protected void onAfterSave(int index) {
    }

    /* added for part 1 of lesson 5 homework new entity */
    protected void onBeforeDelete(int index) {
    }

    /* added for part 1 of lesson 5 homework new entity */
    protected void onAfterDelete(int index) {
    }
}
