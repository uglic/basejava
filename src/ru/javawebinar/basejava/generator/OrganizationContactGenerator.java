package ru.javawebinar.basejava.generator;
/*
 * @author Stepan Shcherbakov /uglic.ru/ 2019
 */

import ru.javawebinar.basejava.generator.contact.LoginGenerator;
import ru.javawebinar.basejava.generator.markov.MarkovChain;
import ru.javawebinar.basejava.generator.markov.MarkovUtil;
import ru.javawebinar.basejava.generator.param.FullNameGeneratorParam;
import ru.javawebinar.basejava.generator.param.IGeneratorParameter;
import ru.javawebinar.basejava.model.Contact;

import java.util.concurrent.ThreadLocalRandom;

public class OrganizationContactGenerator implements IRandomDataGenerator<Contact> {
    private static volatile OrganizationContactGenerator instance;
    private final IRandomDataGenerator<String> loginGenerator = LoginGenerator.getInstance();
    private final MarkovChain markovChain = MarkovUtil.getInstance().getMarkovChain();

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
        String login = loginGenerator.getRandom(new FullNameGeneratorParam(true,
                markovChain.generate(3)));
        return new Contact(markovChain.generate(ThreadLocalRandom.current().nextInt(4)),
                "https://" + login + ".ru");
    }
}
