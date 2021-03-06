package ru.javawebinar.basejava.generator;
/*
 * @author Stepan Shcherbakov /uglic.ru/ 2019
 */

import ru.javawebinar.basejava.generator.markov.MarkovChain;
import ru.javawebinar.basejava.generator.markov.MarkovUtil;
import ru.javawebinar.basejava.generator.param.IGeneratorParameter;

import java.util.concurrent.ThreadLocalRandom;

public class PositionNameGenerator implements IRandomDataGenerator<String> {
    private static volatile PositionNameGenerator instance;
    private final MarkovChain markovChain = MarkovUtil.getInstance().getMarkovChain();

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
        return markovChain.generate(ThreadLocalRandom.current().nextInt(1, 3));
    }
}
