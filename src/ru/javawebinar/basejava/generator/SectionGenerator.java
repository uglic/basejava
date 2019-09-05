package ru.javawebinar.basejava.generator;

import ru.javawebinar.basejava.generator.param.DatesGeneratorParam;
import ru.javawebinar.basejava.generator.param.IGeneratorParameter;
import ru.javawebinar.basejava.generator.param.IsManGeneratorParam;
import ru.javawebinar.basejava.model.*;

import java.time.LocalDate;

public class SectionGenerator implements IRandomDataGenerator<AbstractSection> {
    private final static LocalDate MIN_WORK_DATE = LocalDate.of(1989, 1, 1);
    private static volatile SectionGenerator instance;
    private final IRandomDataGenerator<SimpleTextSection> simpleSectionGenerator = SimpleTextSectionGenerator.getInstance();
    private final IRandomDataGenerator<BulletedTextListSection> bulletedSectionGenerator = BulletedTextListSectionGenerator.getInstance();
    private final IRandomDataGenerator<OrganizationSection> organizationSectionGenerator = OrganizationSectionGenerator.getInstance();

    private SectionGenerator() {
    }

    public static SectionGenerator getInstance() {
        if (instance == null) {
            synchronized (SectionGenerator.class) {
                if (instance == null) {
                    instance = new SectionGenerator();
                }
            }
        }
        return instance;
    }

    @Override
    public AbstractSection getRandom(IGeneratorParameter gp) {
        AbstractSection section;
        IGeneratorParameter paramDeeper;
        switch (gp.getSectionType()) {
            case OBJECTIVE:
            case PERSONAL:
                paramDeeper = new IsManGeneratorParam(gp.isMan());
                section = simpleSectionGenerator.getRandom(paramDeeper);
                break;
            case ACHIEVEMENT:
            case QUALIFICATIONS:
                paramDeeper = new IsManGeneratorParam(gp.isMan());
                section = bulletedSectionGenerator.getRandom(paramDeeper);
                break;
            case EXPERIENCE:
            case EDUCATION:
                paramDeeper = new DatesGeneratorParam(gp.isMan(), MIN_WORK_DATE, LocalDate.now());
                section = organizationSectionGenerator.getRandom(paramDeeper);
                break;
            default:
                section = null;
        }
        return section;
    }
}
