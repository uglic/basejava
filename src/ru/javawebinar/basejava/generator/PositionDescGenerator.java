package ru.javawebinar.basejava.generator;
/*
 * @author Stepan Shcherbakov /uglic.ru/ 2019
 */

import ru.javawebinar.basejava.generator.markov.MarkovChain;
import ru.javawebinar.basejava.generator.markov.MarkovUtil;
import ru.javawebinar.basejava.generator.param.IGeneratorParameter;

import java.util.concurrent.ThreadLocalRandom;

public class PositionDescGenerator implements IRandomDataGenerator<String> {
    private static volatile PositionDescGenerator instance;
    private final MarkovChain markovChain = MarkovUtil.getInstance().getMarkovChain();

    private PositionDescGenerator() {
    }

    public static PositionDescGenerator getInstance() {
        if (instance == null) {
            synchronized (PositionDescGenerator.class) {
                if (instance == null) {
                    instance = new PositionDescGenerator();
                }
            }
        }
        return instance;
    }

    @Override
    public String getRandom(IGeneratorParameter gp) {
        return markovChain.generate(ThreadLocalRandom.current().nextInt(20, 100));
    }
}
