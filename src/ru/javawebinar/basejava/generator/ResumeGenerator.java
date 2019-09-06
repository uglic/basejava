package ru.javawebinar.basejava.generator;

import ru.javawebinar.basejava.generator.contact.LoginGenerator;
import ru.javawebinar.basejava.generator.param.FullNameGeneratorParam;
import ru.javawebinar.basejava.generator.param.IGeneratorParameter;
import ru.javawebinar.basejava.generator.param.LoginContactTypeGeneratorParam;
import ru.javawebinar.basejava.generator.param.SectionTypeGeneratorParam;
import ru.javawebinar.basejava.model.*;

public class ResumeGenerator implements IRandomDataGenerator<Resume> {
    private static volatile ResumeGenerator instance;
    private final IRandomDataGenerator<String> fullNameGenerator = FullNameGenerator.getInstance();
    private final IRandomDataGenerator<String> loginGenerator = LoginGenerator.getInstance();
    private final IRandomDataGenerator<Contact> contactGenerator = ContactGenerator.getInstance();
    private final IRandomDataGenerator<AbstractSection> sectionGenerator = SectionGenerator.getInstance();

    private ResumeGenerator() {
    }

    public static ResumeGenerator getInstance() {
        if (instance == null) {
            synchronized (ResumeGenerator.class) {
                if (instance == null) {
                    instance = new ResumeGenerator();
                }
            }
        }
        return instance;
    }

    @Override
    public Resume getRandom(IGeneratorParameter gp) {
        Resume resume = new Resume(fullNameGenerator.getRandom(gp));
        String login = loginGenerator.getRandom(new FullNameGeneratorParam(gp.isMan(), resume.getFullName()));
        for (ContactTypes contactType : ContactTypes.values()) {
            resume.addContact(contactType, contactGenerator.getRandom(new LoginContactTypeGeneratorParam(gp.isMan(), contactType, login)));
        }
        for (SectionTypes sectionType : SectionTypes.values()) {
            resume.addSection(sectionType, sectionGenerator.getRandom(new SectionTypeGeneratorParam(gp.isMan(), sectionType)));
        }
        return resume;
    }
}
