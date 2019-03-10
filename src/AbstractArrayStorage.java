/**
 * Array based storage for Resumes
 */
public abstract class AbstractArrayStorage implements Storage {
    private static final int STORAGE_LIMIT = 100000;

    /**
     * Storage of resume
     */
    private final Resume[] storage = new Resume[STORAGE_LIMIT];

    /**
     * Count of saved elements
     */
    private int size = 0;
}
