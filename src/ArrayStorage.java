import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage implements Storage {
    private static final int STORAGE_LIMIT = 10000;

    /**
     * Storage of resume
     */
    private final Resume[] storage = new Resume[STORAGE_LIMIT];

    /**
     * Count of saved elements
     */
    private int size = 0;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void update(Resume r) {
        if (r == null) {
            throw new NullPointerException();
        }
        int i = getIndex(r.getUuid());
        if (i != -1) {
            storage[i] = r;
            return;
        }
        System.out.println("Resume uuid=" + r.getUuid() + " not found for update");
        throw new IndexOutOfBoundsException();
    }

    public void save(Resume r) {
        if (r == null) {
            throw new NullPointerException();
        }
        if (size >= storage.length) {
            throw new IndexOutOfBoundsException();
        }
        if (getIndex(r.getUuid()) == -1) {
            storage[size++] = r;
        } else {
            System.out.println("Resume uuid=" + r.getUuid() + " already exists");
        }
    }

    /**
     * Search for Resume by it's uuid
     * By conditions of this task storage is unsorted
     * so full search must be done.
     * Empty string or null instead of String object are allowed
     * Return first founded Resume if several of objects have
     * the same uuid.
     *
     * @param uuid String representation of Resume we are looking for
     * @return Reference to Resume object if founded or null
     */
    public Resume get(String uuid) {
        int i = getIndex(uuid);
        if (i != -1) {
            return storage[i];
        }
        System.out.println("Resume uuid=" + uuid + " not found");
        return null;
    }

    /**
     * Delete first founded Resume from storage and return.
     * Throw an IndexOutOfBoundsException exception
     * if Resume with the uuid was not found.
     *
     * @param uuid String representation of Resume we are looking for
     * @throws IndexOutOfBoundsException if resume with the uuid was not found
     */
    public void delete(String uuid) {
        int i = getIndex(uuid);
        if (i != -1) {
            System.arraycopy(storage, i + 1, storage, i, size - i - 1);
            storage[size - 1] = null; // clear double reference to the same Resume
            size--;
            return;
        }
        System.out.println("Resume uuid=" + uuid + " not found for delete");
        throw new IndexOutOfBoundsException();
    }

    /**
     * Return all saved resumed
     *
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOfRange(storage, 0, size);
    }

    /**
     * Count of saved elements in storage
     *
     * @return Count of saved elements in storage
     */
    public int size() {
        return this.size;
    }

    /**
     * Search for resume with getUuid() == uuid
     *
     * @param uuid String uuid to looking for
     * @return Index in storage or -1 if not found
     */
    private int getIndex(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1; // not found
    }
}
