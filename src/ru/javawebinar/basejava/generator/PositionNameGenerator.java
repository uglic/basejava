package ru.javawebinar.basejava.generator;

import ru.javawebinar.basejava.generator.param.IGeneratorParameter;

import java.util.concurrent.ThreadLocalRandom;

public class PositionNameGenerator implements IRandomDataGenerator<String> {
    private static volatile PositionNameGenerator instance;

    private PositionNameGenerator() {
    }

    public static PositionNameGenerator getInstance() {
        if (instance == null) {
            synchronized (PositionNameGenerator.class) {
                if (instance == null) {
                    instance = new PositionNameGenerator();
                }
            }
        }
        return instance;
    }

    @Override
    public String getRandom(IGeneratorParameter gp) {
        return "Position name " + ThreadLocalRandom.current().nextInt();
    }
}
