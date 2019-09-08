package ru.javawebinar.basejava.generator.loader;
/*
 * @author Stepan Shcherbakov /uglic.ru/ 2019
 */

import java.util.ArrayList;
import java.util.List;

public class ManSurnameFileLineLoader extends AbstractNameFileLineLoader {
    private final static String NAMES = "/men_surnames.txt";

    public ManSurnameFileLineLoader() {
        super(NAMES);
    }

    @Override
    public List<String> defaultList() {
        List<String> list = new ArrayList<>();
        list.add("Простофилин");
        list.add("Трудолюбов");
        list.add("Владимиров");
        list.add("Ласточкин");
        return list;
    }
}
