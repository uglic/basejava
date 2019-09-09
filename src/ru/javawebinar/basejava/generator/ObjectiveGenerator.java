package ru.javawebinar.basejava.generator;
/*
 * @author Stepan Shcherbakov /uglic.ru/ 2019
 */

import ru.javawebinar.basejava.generator.markov.MarkovChain;
import ru.javawebinar.basejava.generator.markov.MarkovUtil;
import ru.javawebinar.basejava.generator.param.IGeneratorParameter;

import java.util.concurrent.ThreadLocalRandom;

public class ObjectiveGenerator implements IRandomDataGenerator<String> {
    private static volatile ObjectiveGenerator instance;
    private final MarkovChain markovChain = MarkovUtil.getInstance().getMarkovChain();

    private ObjectiveGenerator() {
    }

    public static ObjectiveGenerator getInstance() {
        if (instance == null) {
            synchronized (ObjectiveGenerator.class) {
                if (instance == null) {
                    instance = new ObjectiveGenerator();
                }
            }
        }
        return instance;
    }

    @Override
    public String getRandom(IGeneratorParameter gp) {
        return markovChain.generate(ThreadLocalRandom.current().nextInt(5, 25));
    }
}
