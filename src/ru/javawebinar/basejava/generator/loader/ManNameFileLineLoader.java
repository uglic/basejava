package ru.javawebinar.basejava.generator.loader;

import java.util.ArrayList;
import java.util.List;

public class ManNameFileLineLoader extends AbstractNameFileLineLoader {
    private final static String NAMES = "/men_names.txt";

    public ManNameFileLineLoader() {
        super(NAMES);
    }

    @Override
    public List<String> defaultList() {
        List<String> list = new ArrayList<>();
        list.add("Александр");
        list.add("Иван");
        list.add("Олег");
        list.add("Петр");
        return list;
    }
}
