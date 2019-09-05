package ru.javawebinar.basejava.generator;

import ru.javawebinar.basejava.generator.param.IGeneratorParameter;
import ru.javawebinar.basejava.model.Contact;

import java.util.concurrent.ThreadLocalRandom;

public class ContactGenerator implements IRandomDataGenerator<Contact> {
    private static volatile ContactGenerator instance;

    private ContactGenerator() {
    }

    public static ContactGenerator getInstance() {
        if (instance == null) {
            synchronized (ContactGenerator.class) {
                if (instance == null) {
                    instance = new ContactGenerator();
                }
            }
        }
        return instance;
    }

    @Override
    public Contact getRandom(IGeneratorParameter gp) {
        Contact contact = new Contact("Sample contact " + ThreadLocalRandom.current().nextInt(), gp.getContactType());

        return contact;
    }
}
