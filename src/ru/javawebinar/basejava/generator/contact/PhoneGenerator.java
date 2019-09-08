package ru.javawebinar.basejava.generator.contact;
/*
 * @author Stepan Shcherbakov /uglic.ru/ 2019
 */

import ru.javawebinar.basejava.generator.IRandomDataGenerator;
import ru.javawebinar.basejava.generator.param.IGeneratorParameter;

import java.util.concurrent.ThreadLocalRandom;

public class PhoneGenerator implements IRandomDataGenerator<String> {
    private static volatile PhoneGenerator instance;

    private PhoneGenerator() {
    }

    public static PhoneGenerator getInstance() {
        if (instance == null) {
            synchronized (PhoneGenerator.class) {
                if (instance == null) {
                    instance = new PhoneGenerator();
                }
            }
        }
        return instance;
    }

    @Override
    public String getRandom(IGeneratorParameter gp) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int zone = random.nextInt(1_000);
        int p1 = random.nextInt(1_000);
        int p2 = random.nextInt(100);
        int p3 = random.nextInt(100);
        return String.format("+7(%03d)%03d-%02d-%02d", zone, p1, p2, p3);
    }
}
