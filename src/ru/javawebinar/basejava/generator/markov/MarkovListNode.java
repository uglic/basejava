package ru.javawebinar.basejava.generator.markov;/*
 * @author Stepan Shcherbakov /uglic.ru/ 2019
 * License MIT
 */

import java.util.Set;
import java.util.TreeSet;

public class MarkovListNode {
    private String word;
    private int totalCount;
    private Set<MarkovChildNode> children = new TreeSet<>();

    public MarkovListNode(String word) {
        this.word = word;
    }

    public void addNextWord(String word) {
//        MarkovChildNode testNode = new MarkovChildNode(word, 0,0);
//        if(children.contains(testNode)){
//            test
//        } else {
//
//        }
//
//
//        children.computeIfPresent(word, (k, v) -> {
//            v.count++;
//            children.values().stream()
//                    .filter((c) -> c.position > v.position)
//                    .forEach((c) -> c.position++);
//            return v;
//        });
//        children.computeIfAbsent(word, (k) -> children.put(k, new MarkovChildNode(1, totalCount)));
//        totalCount++;
    }

    public String getNextRandomWord() {
//        int position = ThreadLocalRandom.current().nextInt(totalCount);
//        MarkovChildNode child;
//        Iterator<String> it = children.keySet().iterator();
//        for (; it.hasNext(); ) {
//            child = children.get(it.next());
//        }
        return null;
    }
}
