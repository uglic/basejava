package ru.javawebinar.basejava.generator;

import ru.javawebinar.basejava.generator.param.ContactTypeGeneratorParam;
import ru.javawebinar.basejava.generator.param.IGeneratorParameter;
import ru.javawebinar.basejava.generator.param.SectionTypeGeneratorParam;
import ru.javawebinar.basejava.model.*;

public class ResumeGenerator implements IRandomDataGenerator<Resume> {
    private static volatile ResumeGenerator instance;
    private final IRandomDataGenerator<String> fullNameGenerator = FullNameGenerator.getInstance();
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
        for (ContactTypes contactType : ContactTypes.values()) {
            resume.addContact(contactType, contactGenerator.getRandom(new ContactTypeGeneratorParam(gp.isMan(), contactType)));
        }
        for (SectionTypes sectionType : SectionTypes.values()) {
            resume.addSection(sectionType, sectionGenerator.getRandom(new SectionTypeGeneratorParam(gp.isMan(), sectionType)));
        }
        return resume;
    }
}
