package ru.javawebinar.basejava.generator;
/*
 * @author Stepan Shcherbakov /uglic.ru/ 2019
 */

import ru.javawebinar.basejava.generator.contact.LoginGenerator;
import ru.javawebinar.basejava.generator.param.IGeneratorParameter;
import ru.javawebinar.basejava.generator.param.IsManGeneratorParam;
import ru.javawebinar.basejava.model.Contact;
import ru.javawebinar.basejava.model.Organization;
import ru.javawebinar.basejava.util.DateUtil;

import java.time.LocalDate;
import java.time.Period;
import java.util.concurrent.ThreadLocalRandom;

public class OrganizationGenerator implements IRandomDataGenerator<Organization> {
    private final static int MAX_POSITION_ON_SAME_ORGANIZATION = 3;
    private static volatile OrganizationGenerator instance;
    private final IRandomDataGenerator<Contact> contactGenerator = OrganizationContactGenerator.getInstance();
    private final IRandomDataGenerator<String> positionNameGenerator = PositionNameGenerator.getInstance();
    private final IRandomDataGenerator<String> positionDescGenerator = PositionDescGenerator.getInstance();
    private final IRandomDataGenerator<String> loginGenerator = LoginGenerator.getInstance();

    private OrganizationGenerator() {
    }

    public static OrganizationGenerator getInstance() {
        if (instance == null) {
            synchronized (OrganizationGenerator.class) {
                if (instance == null) {
                    instance = new OrganizationGenerator();
                }
            }
        }
        return instance;
    }

    @Override
    public Organization getRandom(IGeneratorParameter gp) {
        LocalDate minDate = gp.getDateFrom();
        LocalDate maxDate = gp.getDateTo();
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int positionCount = random.nextInt(1, MAX_POSITION_ON_SAME_ORGANIZATION);
        LocalDate[] dates = new LocalDate[positionCount + 1];
        for (int i = 0; i <= positionCount; i++) {
            dates[i] = DateUtil.getRandomBetween(minDate, maxDate, random);
            minDate = dates[i];
        }


        Organization.Position[] positions = new Organization.Position[positionCount];
        IGeneratorParameter paramDeeper = new IsManGeneratorParam(gp.isMan());
        for (int i = 0; i < positionCount; i++) {
            Period period = Period.between(dates[i], dates[i + 1]);
            LocalDate date2 = (period.toTotalMonths() > 0) ? dates[i + 1].minusMonths(1) : dates[i];
            date2 = date2.withDayOfMonth(date2.lengthOfMonth());
            if (date2.isBefore(dates[i])) {
                date2 = dates[i];
            }
            positions[i] = new Organization.Position(dates[i], date2,
                    positionNameGenerator.getRandom(paramDeeper),
                    positionDescGenerator.getRandom(paramDeeper));
        }
        return new Organization(contactGenerator.getRandom(gp), positions);
    }
}
