package ru.javawebinar.basejava.generator.contact;

import ru.javawebinar.basejava.generator.IRandomDataGenerator;
import ru.javawebinar.basejava.generator.param.IGeneratorParameter;
import ru.javawebinar.basejava.model.Contact;
import ru.javawebinar.basejava.model.ContactTypes;

public class HomeSiteContactGenerator implements IRandomDataGenerator<Contact> {
    private static volatile HomeSiteContactGenerator instance;

    private HomeSiteContactGenerator() {
    }

    public static HomeSiteContactGenerator getInstance() {
        if (instance == null) {
            synchronized (HomeSiteContactGenerator.class) {
                if (instance == null) {
                    instance = new HomeSiteContactGenerator();
                }
            }
        }
        return instance;
    }

    @Override
    public Contact getRandom(IGeneratorParameter gp) {
        String login = gp.getLogin();
        return new Contact("https://www." + login + ".ru/", ContactTypes.HOMESITE);
    }
}
