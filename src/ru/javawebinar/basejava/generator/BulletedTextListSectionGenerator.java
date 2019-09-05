package ru.javawebinar.basejava.generator;

import ru.javawebinar.basejava.generator.param.IGeneratorParameter;
import ru.javawebinar.basejava.generator.param.IsManGeneratorParam;
import ru.javawebinar.basejava.model.BulletedTextListSection;

import java.util.concurrent.ThreadLocalRandom;

public class BulletedTextListSectionGenerator implements IRandomDataGenerator<BulletedTextListSection> {
    private static final int MAX_ITEMS = 15;
    private static volatile BulletedTextListSectionGenerator instance;
    private final IRandomDataGenerator<String> itemTextGenerator = PositionDescGenerator.getInstance();

    private BulletedTextListSectionGenerator() {
    }

    public static BulletedTextListSectionGenerator getInstance() {
        if (instance == null) {
            synchronized (BulletedTextListSectionGenerator.class) {
                if (instance == null) {
                    instance = new BulletedTextListSectionGenerator();
                }
            }
        }
        return instance;
    }

    @Override
    public BulletedTextListSection getRandom(IGeneratorParameter parameter) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int itemsCount = random.nextInt(0, MAX_ITEMS);
        String[] items = new String[itemsCount];
        for (int i = 0; i < itemsCount; i++) {
            items[i] = itemTextGenerator.getRandom(new IsManGeneratorParam(parameter.isMan()));
        }
        return new BulletedTextListSection(items);
    }
}
