package ru.javawebinar.basejava.generator;

import ru.javawebinar.basejava.generator.contact.*;
import ru.javawebinar.basejava.generator.param.IGeneratorParameter;
import ru.javawebinar.basejava.model.Contact;
import ru.javawebinar.basejava.model.ContactTypes;
/*
 * @author Stepan Shcherbakov /uglic.ru/ 2019
 */

import java.util.concurrent.ThreadLocalRandom;

public class ContactGenerator implements IRandomDataGenerator<Contact> {
    private static volatile ContactGenerator instance;
    private final IRandomDataGenerator<String> phoneGenerator = PhoneGenerator.getInstance();
    private final IRandomDataGenerator<String> emailGenerator = EmailGenerator.getInstance();
    private final IRandomDataGenerator<String> skypeGenerator = SkypeGenerator.getInstance();
    private final IRandomDataGenerator<Contact> linkedinGenerator = LinkedInContactGenerator.getInstance();
    private final IRandomDataGenerator<Contact> githubGenerator = GithubContactGenerator.getInstance();
    private final IRandomDataGenerator<Contact> stackGenerator = StackOverflowContactGenerator.getInstance();
    private final IRandomDataGenerator<Contact> homesiteGenerator = HomeSiteContactGenerator.getInstance();

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
        switch (gp.getContactType()) {
            case PHONE:
                return new Contact(phoneGenerator.getRandom(gp), ContactTypes.PHONE);
            case EMAIL:
                return new Contact(emailGenerator.getRandom(gp), ContactTypes.EMAIL);
            case SKYPE:
                return new Contact(skypeGenerator.getRandom(gp), ContactTypes.SKYPE);
            case LINKEDIN:
                return linkedinGenerator.getRandom(gp);
            case GITHUB:
                return githubGenerator.getRandom(gp);
            case STACKOVERFLOW:
                return stackGenerator.getRandom(gp);
            case HOMESITE:
                return homesiteGenerator.getRandom(gp);
            default:
                return new Contact("Sample contact " + ThreadLocalRandom.current().nextInt(), gp.getContactType());
        }
    }
}
