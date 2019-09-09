package ru.javawebinar.basejava.generator.loader;
/*
 * @author Stepan Shcherbakov /uglic.ru/ 2019
 */

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BookWordFileLineLoader extends AbstractNameFileLineLoader {
    private final static String NAMES = "/book.txt";
    private final String filename;

    public BookWordFileLineLoader(String filename) {
        super(filename);
        this.filename = filename;
    }

    public BookWordFileLineLoader() {
        super(NAMES);
        this.filename = NAMES;
    }

    @Override
    public String filterRegex() {
        return ".*";
    }

    @Override
    public List<String> parseLine(String line) {
        Pattern regex = Pattern.compile("[a-zA-Z.,!абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ]+");
        Matcher match = regex.matcher(line);
        List<String> words = new LinkedList<>(); // always must be single direction looking
        while(match.find()){
            words.add(match.group());
        }
        return words;
    }

    @Override
    public List<String> defaultList() {
        List<String> list = new ArrayList<>();
        list.add("мир");
        list.add("дверь");
        list.add("окно");
        list.add("паразит.");
        return list;
    }
}
