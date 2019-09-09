package ru.javawebinar.basejava.generator.markov;/*
 * @author Stepan Shcherbakov /uglic.ru/ 2019
 * License MIT
 */

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

class MarkovListNode {
    private int totalCount;
    private boolean isPositionsActual;
    private final Map<String, MarkovChildNode> childrenByName = new LinkedHashMap<>();
    private final List<MarkovChildNode> children = new ArrayList<>();

    void addNextWord(String word) {
        if (!isPositionsActual) {
            childrenByName.compute(word, (k, v) -> {
                MarkovChildNode node;
                if (v == null) {
                    node = new MarkovChildNode(k);
                    children.add(node);
                } else {
                    node = v;
                }
                node.count++;
                return node;
            });
            totalCount++;
        } else {
            throw new IllegalStateException("List of words is already closed to addition");
        }
    }

    String getNextRandomWord() {
        int position = ThreadLocalRandom.current().nextInt(totalCount);
        return getWordByPosition(position);
    }

    String getWordByPosition(int position) {
        if (!isPositionsActual) syncPositions();
        MarkovChildNode test = new MarkovChildNode(null, 0, position);
        int search = Collections.binarySearch(children, test, MarkovChildNode::compareByPosition);
        if (search < 0) {
            int index = -search - 1 - 1;
            test = children.get(index);
            if (index == children.size() - 1 && test.position + test.count <= position) {
                throw new IndexOutOfBoundsException("Maximum index value is " + (totalCount - 1));
            } else {
                return children.get(-search - 1 - 1).word;

            }
        } else {
            return children.get(search).word;
        }
    }

    private void syncPositions() {
        int position = 0;
        for (MarkovChildNode node : children) {
            node.position = position;
            position += node.count;
        }
        if (position != totalCount) {
            throw new IllegalStateException("Total of words is not equal count's sum");
        }
        isPositionsActual = true;
    }
}

