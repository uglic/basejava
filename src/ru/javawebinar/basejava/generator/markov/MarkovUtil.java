package ru.javawebinar.basejava.generator.markov;

/*
 * @author Stepan Shcherbakov /uglic.ru/ 2019
 */

import ru.javawebinar.basejava.generator.loader.BookWordFileLineLoader;

public class MarkovUtil {
    private static volatile MarkovUtil instance;
    private final static MarkovChain markovChain = new MarkovChain(new BookWordFileLineLoader().load());

    private MarkovUtil() {
    }

    public static MarkovUtil getInstance() {
        if (instance == null) {
            synchronized (MarkovUtil.class) {
                if (instance == null) {
                    instance = new MarkovUtil();
                }
            }
        }
        return instance;
    }

    public MarkovChain getMarkovChain() {
        return markovChain;
    }
}
