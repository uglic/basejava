package ru.javawebinar.basejava.generator.contact;
/*
 * @author Stepan Shcherbakov /uglic.ru/ 2019
 */

import ru.javawebinar.basejava.generator.IRandomDataGenerator;
import ru.javawebinar.basejava.generator.param.IGeneratorParameter;
import ru.javawebinar.basejava.model.Contact;
import ru.javawebinar.basejava.model.ContactTypes;

public class LinkedInContactGenerator implements IRandomDataGenerator<Contact> {
    private static volatile LinkedInContactGenerator instance;

    private LinkedInContactGenerator() {
    }

    public static LinkedInContactGenerator getInstance() {
        if (instance == null) {
            synchronized (LinkedInContactGenerator.class) {
                if (instance == null) {
                    instance = new LinkedInContactGenerator();
                }
            }
        }
        return instance;
    }

    @Override
    public Contact getRandom(IGeneratorParameter gp) {
        String login = gp.getLogin();
        return new Contact("https://www.linkedin.com/in/" + login, ContactTypes.LINKEDIN);
    }
}
