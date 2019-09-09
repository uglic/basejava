package ru.javawebinar.basejava.generator.markov;/*
 * @author Stepan Shcherbakov /uglic.ru/ 2019
 * License MIT
 */

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MarkovChain {
    private final HashMap<String, MarkovListNode> wordChain = new HashMap<>();

    public MarkovChain(List<String> words) {
        MarkovListNode prevList = null;
        for (String word : words) {
            String currWord = word.toLowerCase();
            if (prevList != null) {
                prevList.addNextWord(currWord);
            }
            prevList = wordChain.computeIfAbsent(currWord, (k) -> new MarkovListNode());
        }
        if (words.size() > 0 && wordChain.size() > 0) {
            prevList.addNextWord(words.get(0));
        }
    }

    public String generate(int wordCount) {
        if (wordCount <= 0 || wordChain.size() == 0) return null;
        MarkovListNode list = getRandomListNode();
        if (list == null) return null;
        boolean isUpperFirstLetter = true;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < wordCount; i++) {
            String word = list.getNextRandomWord();
            list = wordChain.get(word);
            if (isUpperFirstLetter) word = upperFirstLetter(word);
            if (i != 0) sb.append(" ");
            sb.append(word);
            isUpperFirstLetter = ".!".contains(word.substring(word.length() - 1));
            if (list == null) break;
        }
        if (sb.length() > 0 && !".,!".contains(sb.substring(sb.length() - 1))) {
            sb.append(".");
        }
        return sb.toString();
    }

    private MarkovListNode getRandomListNode() {
        int position = ThreadLocalRandom.current().nextInt(wordChain.size());
        int counter = 0;
        Iterator<String> iterator = wordChain.keySet().iterator();
        for (; iterator.hasNext() && counter < position; counter++) {
            iterator.next();
        }
        if (!iterator.hasNext()) return null;
        return wordChain.get(iterator.next());
    }

    private String upperFirstLetter(String word) {
        if (word == null || word.isEmpty()) {
            return "";
        } else {
            return word.substring(0, 1).toUpperCase().concat(word.substring(1));
        }
    }
}
