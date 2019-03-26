package ru.javawebinar.basejava.model;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ResumeTest {
    private final static String FULLNAME_1 = "Abab Babov";
    private final static String UUID_1 = "uuid1";
    private final static Resume RESUME_1 = new Resume(UUID_1, FULLNAME_1);
    private final static Resume RESUME_2 = new Resume("uuid2", "Baba Cacov");
    private final static Resume RESUME_1_COPY1 = new Resume(UUID_1, FULLNAME_1);
    private final static Resume RESUME_1_COPY2 = new Resume(UUID_1, FULLNAME_1);
    private final static Resume RESUME_1_COPY_NEWUUID = new Resume(FULLNAME_1);
    private final static Resume RESUME_1_COPY_NEWNAME = new Resume(UUID_1, FULLNAME_1 + "new");

    @Test(expected = NullPointerException.class)
    public void createNullUuid() {
        new Resume(null, FULLNAME_1);
    }

    @Test(expected = NullPointerException.class)
    public void createNullFullName() {
        new Resume(UUID_1, null);
    }

    @Test
    public void getUuid() {
        assertEquals(UUID_1, RESUME_1.getUuid());
    }

    @Test
    public void getFullName() {
        assertEquals(FULLNAME_1, RESUME_1.getFullName());
    }

    @Test
    public void equalsReflexive() {
        assertTrue(RESUME_1.equals(RESUME_1));
    }

    @Test
    public void equalsSymmetric() {
        assertTrue(RESUME_1.equals(RESUME_1_COPY1));
        assertTrue(RESUME_1_COPY1.equals(RESUME_1));
        assertFalse(RESUME_1.equals(RESUME_2));
        assertFalse(RESUME_2.equals(RESUME_1));
    }

    @Test
    public void equalsTransitive() {
        assertTrue(RESUME_1.equals(RESUME_1_COPY1));
        assertTrue(RESUME_1_COPY1.equals(RESUME_1_COPY2));
        assertTrue(RESUME_1.equals(RESUME_1_COPY2));
    }

    @Test
    public void equalsConsistent() {
        // consistent: multiple invocations
        assertTrue(RESUME_1.equals(RESUME_1_COPY1));
        assertTrue(RESUME_1.equals(RESUME_1_COPY1));
        assertTrue(RESUME_1.equals(RESUME_1_COPY1));
        assertFalse(RESUME_1.equals(RESUME_2));
        assertFalse(RESUME_1.equals(RESUME_2));
        assertFalse(RESUME_1.equals(RESUME_2));
    }

    @Test
    public void equalsNull() {
        assertFalse(RESUME_1.equals(null));
    }

    @Test
    public void equalsClass() {
        assertFalse(RESUME_1.equals(new Object()));
    }

    @Test
    public void hashCodeConsistent() {
        int hash1 = RESUME_1.hashCode();

        // some work with object
        RESUME_1.getUuid();
        RESUME_1.getFullName();

        int hash2 = RESUME_1.hashCode();
        assertTrue(hash1 == hash2);
    }

    @Test
    public void hashCodeSameForEquals() {
        int hash1 = RESUME_1.hashCode();
        int hash2 = RESUME_1_COPY1.hashCode();
        assertTrue(RESUME_1.equals(RESUME_1_COPY1));
        assertTrue(hash1 == hash2);
    }

    @Test
    public void hashCodeCanBeDifferent() {
        int hash1 = RESUME_1.hashCode();
        int hash2 = RESUME_2.hashCode();
        assertFalse(RESUME_1.equals(RESUME_2));
        assertFalse(hash1 == hash2);
    }

    @Test
    public void toStringCanBeDifferent() {
        assertFalse(RESUME_1.toString().equals(RESUME_2.toString()));
    }

    @Test
    public void compareToDifferentAll() {
        assertEquals(0, RESUME_1.compareTo(RESUME_1));
        assertEquals(-1, RESUME_1.compareTo(RESUME_2));
        assertEquals(1, RESUME_2.compareTo(RESUME_1));
    }

    @Test
    public void compareToDifferentUuidOnly() {
        assertNotEquals(0, RESUME_1.compareTo(RESUME_1_COPY_NEWUUID));
    }

    @Test
    public void compareToDifferentNameOnly() {
        assertNotEquals(0, RESUME_1.compareTo(RESUME_1_COPY_NEWNAME));
    }
}