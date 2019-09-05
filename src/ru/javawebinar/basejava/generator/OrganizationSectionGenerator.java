package ru.javawebinar.basejava.generator;

import ru.javawebinar.basejava.generator.param.DatesGeneratorParam;
import ru.javawebinar.basejava.generator.param.IGeneratorParameter;
import ru.javawebinar.basejava.model.Organization;
import ru.javawebinar.basejava.model.OrganizationSection;
import ru.javawebinar.basejava.util.DateUtil;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

public class OrganizationSectionGenerator implements IRandomDataGenerator<OrganizationSection> {
    private static final int MAX_WORK_PLACES = 7;
    private static volatile OrganizationSectionGenerator instance;
    private final IRandomDataGenerator<Organization> organizationGenerator = OrganizationGenerator.getInstance();

    private OrganizationSectionGenerator() {
    }

    public static OrganizationSectionGenerator getInstance() {
        if (instance == null) {
            synchronized (OrganizationSectionGenerator.class) {
                if (instance == null) {
                    instance = new OrganizationSectionGenerator();
                }
            }
        }
        return instance;
    }

    @Override
    public OrganizationSection getRandom(IGeneratorParameter gp) {
        LocalDate minDate = gp.getDateFrom();
        LocalDate maxDate = gp.getDateTo();
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int workCount = random.nextInt(1, MAX_WORK_PLACES);
        LocalDate[] dates = new LocalDate[workCount + 1];
        for (int i = 0; i <= workCount; i++) {
            dates[i] = DateUtil.getRandomBetween(minDate, maxDate, random);
            minDate = dates[i];
        }
        Organization[] organizations = new Organization[workCount];
        for (int i = 0; i < workCount; i++) {
            organizations[i] = organizationGenerator.getRandom(new DatesGeneratorParam(gp.isMan(), dates[i], dates[i + 1]));
        }
        return new OrganizationSection(organizations);
    }

}
