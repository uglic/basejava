package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {
    private static final Comparator<Resume> RESUME_COMPARATOR_FULLNAME = (o1, o2) -> o1.getFullName().compareTo(o2.getFullName());
    private static final Comparator<Resume> RESUME_COMPARATOR_UUID = (o1, o2) -> o1.getUuid().compareTo(o2.getUuid());
    private static final Comparator<Resume> RESUME_COMPARATOR = RESUME_COMPARATOR_FULLNAME.thenComparing(RESUME_COMPARATOR_UUID);

    @Override
    protected Integer getSearchKey(String uuid) {
        for (int i = 0; i < size; i++) {
            if (uuid.equals(storage[i].getUuid())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected int reorder(Object searchKey) {
        if ((int) searchKey >= 0) {
            storage[(int) searchKey] = storage[size - 1];
        }
        return size;
    }

    @Override
    public List<Resume> getAllSorted() {
        Resume[] resumes = Arrays.copyOfRange(storage, 0, size);
        Arrays.sort(resumes, RESUME_COMPARATOR);
        return Arrays.asList(resumes);
    }
}