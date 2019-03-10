import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private final Resume[] storage = new Resume[10_000];
    private int size = 0;
    private static final int INCORRECT_INDEX = -1; // must be below zero

    void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    void update(Resume r) {
        if (r == null) {
            System.out.println("Null not allowed for resume");
        } else {
            int i = getPosition(r.getUuid()); // to avoid double call
            if (i != INCORRECT_INDEX) {
                storage[i] = r;
            } else {
                System.out.println("Resume for update is not exists");
            }
        }
    }

    void save(Resume r) {
        if (r == null) {
            System.out.println("Null not allowed for resume");
        } else if (size >= storage.length) {
            System.out.println("Resume to save is not exists");
        } else if (getPosition(r.getUuid()) == INCORRECT_INDEX) {
            storage[size++] = r;
            System.out.println("Resume with uuid=" + r.getUuid() + " added");
        }
    }

    Resume get(String uuid) {
        int i = getPosition(uuid); // to avoid double call
        if (i != INCORRECT_INDEX) {
            return storage[i];
        } else {
            System.out.println("Resume for get is not exists");
            return null;
        }
    }

    void delete(String uuid) {
        int i = getPosition(uuid); // to avoid double call
        if (i != INCORRECT_INDEX) {
            if (i < size - 1) {
                storage[i] = storage[size - 1]; // ignore sorting order
            }
            storage[size - 1] = null; // clear double reference to the same Resume
            size--;
            System.out.println("Resume with uuid=" + uuid + " was deleted");
        } else {
            System.out.println("Resume for delete is not exists");
        }
    }

    Resume[] getAll() {
        return Arrays.copyOfRange(storage, 0, size);
    }

    int size() {
        return this.size;
    }

    private int getPosition(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return INCORRECT_INDEX;
    }
}
