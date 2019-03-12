package ru.javawebinar.basejava.storage;

public class ArrayStorageTest extends AbstractArrayStorageTest {
    @Override
    public void setUpTestObjects(Object caller) throws Exception {
        storage = new ArrayStorage(); //super.setUpTestObjects(caller);
    }
}