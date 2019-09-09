package ru.javawebinar.basejava.generator.markov;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/*
 * @author Stepan Shcherbakov /uglic.ru/ 2019
 * License MIT
 */
class MarkovListNodeTest {
    private static MarkovListNode markovListNode;

    @BeforeAll
    public static void fill() {
        markovListNode = new MarkovListNode();
        for (int i = 1; i < 10; i++) {
            for (int j = 1; j <= i; j++) {
                markovListNode.addNextWord("" + i);
            }
        }
    }

    @Test
    void getWordByPosition() {
        String[] expected4 = new String[]{"1", "2", "2", "3", "3", "3", "4", "4", "4", "4"};
        String[] actual = new String[10];
        for (int i = 0; i < 10; i++) {
            actual[i] = markovListNode.getWordByPosition(i);
        }
        assertArrayEquals(expected4, actual);
        assertDoesNotThrow(() -> markovListNode.getWordByPosition(44));
        assertThrows(IndexOutOfBoundsException.class, () -> markovListNode.getWordByPosition(45));
        assertThrows(IndexOutOfBoundsException.class, () -> markovListNode.getWordByPosition(-1));
    }

    @Test
    void getNextRandomWord() {
        List<Integer> expected100 = new ArrayList<>(Arrays.asList(100, 200, 300, 400, 500, 600, 700, 800, 900));
        HashMap<String, Integer> wordCount = new HashMap<>();
        for (int i = 0; i < 1_000_000; i++) {
            wordCount.merge(markovListNode.getNextRandomWord(), 1, Integer::sum);
        }
        double base = wordCount.get("1") / 100d;
        wordCount.entrySet().forEach(e -> {
            int normalized = (int) (e.getValue() / base);
            int rest = normalized % 100;
            if (rest <= 10) {
                normalized = normalized - rest;
            } else if (rest >= 90) normalized = normalized - rest + 100;
            e.setValue(normalized);
        });
        List<Integer> frequency = wordCount.values().stream().sorted().collect(Collectors.toList());
        assertEquals(expected100, frequency);
    }
}