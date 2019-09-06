package ru.javawebinar.basejava.generator.contact;

import ru.javawebinar.basejava.generator.IRandomDataGenerator;
import ru.javawebinar.basejava.generator.param.IGeneratorParameter;
import ru.javawebinar.basejava.model.Contact;
import ru.javawebinar.basejava.model.ContactTypes;

import java.util.concurrent.ThreadLocalRandom;

public class StackOverflowContactGenerator implements IRandomDataGenerator<Contact> {
    private static volatile StackOverflowContactGenerator instance;

    private StackOverflowContactGenerator() {
    }

    public static StackOverflowContactGenerator getInstance() {
        if (instance == null) {
            synchronized (StackOverflowContactGenerator.class) {
                if (instance == null) {
                    instance = new StackOverflowContactGenerator();
                }
            }
        }
        return instance;
    }

    @Override
    public Contact getRandom(IGeneratorParameter gp) {
        return new Contact("https://stackoverflow.com/users/"
                + ThreadLocalRandom.current().nextInt(612_535, 900_000), ContactTypes.STACKOVERFLOW);
    }
}
