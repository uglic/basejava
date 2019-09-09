package ru.javawebinar.basejava.generator.markov;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.javawebinar.basejava.generator.loader.BookWordFileLineLoader;

import java.util.List;

/*
 * @author Stepan Shcherbakov /uglic.ru/ 2019
 * License MIT
 */
class MarkovChainTest {
    private static MarkovChain markovChain;

    @BeforeAll
    static void load() {
        List<String> words = new BookWordFileLineLoader().load();
        markovChain = new MarkovChain(words);
    }

    @Test
    void generate() {
        System.out.println(markovChain.generate(10));
    }
}