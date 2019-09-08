package ru.javawebinar.basejava.generator;

import ru.javawebinar.basejava.generator.loader.*;
import ru.javawebinar.basejava.generator.param.IGeneratorParameter;
/*
 * @author Stepan Shcherbakov /uglic.ru/ 2019
 */

import java.util.concurrent.ThreadLocalRandom;

public class FullNameGenerator implements IRandomDataGenerator<String> {
    private static volatile FullNameGenerator instance;

    private final LazyList<String> manNames = new LazyList<>(new ManNameFileLineLoader());
    private final LazyList<String> manPatronymics = new LazyList<>(new ManPatronymicModifyingLoader(manNames));
    private final LazyList<String> manSurnames = new LazyList<>(new ManSurnameFileLineLoader());
    private final LazyList<String> womanNames = new LazyList<>(new WomanNameFileLineLoader());
    private final LazyList<String> womanPatronymics = new LazyList<>(new WomanPatronymicModifyingLoader(manNames));
    private final LazyList<String> womanSurnames = new LazyList<>(new WomanSurnameModifyingLoader(manSurnames));

    private FullNameGenerator() {
    }

    public static FullNameGenerator getInstance() {
        if (instance == null) {
            synchronized (FullNameGenerator.class) {
                if (instance == null) {
                    instance = new FullNameGenerator();
                }
            }
        }
        return instance;
    }

    @Override
    public String getRandom(IGeneratorParameter parameter) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        if (parameter.isMan()) {
            return getRandomFromList(manSurnames, random)
                    .concat(" " + getRandomFromList(manNames, random)
                            .concat(" " + getRandomFromList(manPatronymics, random)));
        } else {
            return getRandomFromList(womanSurnames, random)
                    .concat(" " + getRandomFromList(womanNames, random)
                            .concat(" " + getRandomFromList(womanPatronymics, random)));
        }
    }

    private String getRandomFromList(LazyList<String> lazyList, ThreadLocalRandom random) {
        String name = lazyList.get(random.nextInt(lazyList.size()));
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }
}
