package ru.javawebinar.basejava.generator;
/*
 * @author Stepan Shcherbakov /uglic.ru/ 2019
 */

import ru.javawebinar.basejava.generator.param.IGeneratorParameter;
import ru.javawebinar.basejava.model.Contact;
import ru.javawebinar.basejava.model.ContactTypes;

import java.util.concurrent.ThreadLocalRandom;

public class OrganizationContactGenerator implements IRandomDataGenerator<Contact> {
    private static volatile OrganizationContactGenerator instance;

    private OrganizationContactGenerator() {
    }

    public static OrganizationContactGenerator getInstance() {
        if (instance == null) {
            synchronized (OrganizationContactGenerator.class) {
                if (instance == null) {
                    instance = new OrganizationContactGenerator();
                }
            }
        }
        return instance;
    }

    @Override
    public Contact getRandom(IGeneratorParameter gp) {
        return new Contact("Sample contact " + ThreadLocalRandom.current().nextInt(), ContactTypes.HOMESITE);
    }
}
