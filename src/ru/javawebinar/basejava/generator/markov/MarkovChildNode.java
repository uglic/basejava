package ru.javawebinar.basejava.generator.markov;
/*
 * @author Stepan Shcherbakov /uglic.ru/ 2019
 * License MIT
 */

class MarkovChildNode {
    String word;
    int count;       // count of word in th chain
    int position; // sum of count for before-in-chain words

    MarkovChildNode(String word, int count, int position) {
        this.word = word;
        this.count = count;
        this.position = position;
    }

    MarkovChildNode(String word) {
        this.word = word;
    }

    static int compareByPosition(MarkovChildNode node1, MarkovChildNode node2) {
        return Integer.compare(node1.position, node2.position);
    }
}
