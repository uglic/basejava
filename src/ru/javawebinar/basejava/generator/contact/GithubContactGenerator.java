package ru.javawebinar.basejava.generator.contact;
/*
 * @author Stepan Shcherbakov /uglic.ru/ 2019
 */

import ru.javawebinar.basejava.generator.IRandomDataGenerator;
import ru.javawebinar.basejava.generator.param.IGeneratorParameter;
import ru.javawebinar.basejava.model.Contact;
import ru.javawebinar.basejava.model.ContactTypes;

public class GithubContactGenerator implements IRandomDataGenerator<Contact> {
    private static volatile GithubContactGenerator instance;

    private GithubContactGenerator() {
    }

    public static GithubContactGenerator getInstance() {
        if (instance == null) {
            synchronized (GithubContactGenerator.class) {
                if (instance == null) {
                    instance = new GithubContactGenerator();
                }
            }
        }
        return instance;
    }

    @Override
    public Contact getRandom(IGeneratorParameter gp) {
        String login = gp.getLogin();
        return new Contact("https://github.com/" + login, ContactTypes.GITHUB);
    }
}
