package ru.javawebinar.basejava.generator.markov;
/*
 * @author Stepan Shcherbakov /uglic.ru/ 2019
 * License MIT
 */

class MarkovChildNode implements Comparable<MarkovChildNode> {
    String word;
    int count;       // count of word in th chain
    int position; // sum of count for before-in-chain words

    MarkovChildNode(String word, int count, int position) {
        this.word = word;
        this.count = count;
        this.position = position;
    }

    @Override
    public int compareTo(MarkovChildNode o) {
        return word.compareTo(o.word);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MarkovChildNode child = (MarkovChildNode) o;

        return word.equals(child.word);
    }

    @Override
    public int hashCode() {
        return word.hashCode();
    }
}
