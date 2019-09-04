package ru.javawebinar.basejava.generator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GeneratedData {
    public final static String MEN_NAMES = "config/men_names.txt";
    public final static String WOMEN_NAMES = "config/women_names.txt";

    public static List<String> getMenNames() {
        List<String> list = getNames(MEN_NAMES);
        if (list.isEmpty()) {
            list.add("Александр");
            list.add("Иван");
            list.add("Олег");
            list.add("Петр");
        }
        return list;
    }

    public static List<String> getWomenNames() {
        List<String> list = getNames(WOMEN_NAMES);
        if (list.isEmpty()) {
            list.add("Анна");
            list.add("Василиса");
            list.add("Наталья");
            list.add("Ольга");
        }
        return list;
    }

    public static List<String> getMenPatronymics() {
        List<String> listNames = getMenNames();
        List<String> list = new ArrayList<>();
        listNames.forEach((name) -> {
            String lastChar = name.substring(name.length() - 1);
            if ("аеёу".contains(lastChar)) {
                list.add(name + "вич");
            } else if ("оыэюя".contains(lastChar)) {
                list.add(name + "нович");
            } else if ("бвгдзклмнпрстфхцчшщ".contains(lastChar)) {
                list.add(name + "ович");
            } else if ("жиъ".contains(lastChar)) {
                list.add(name + "eвич");
            } else if ("ь".contains(lastChar)) {
                list.add(name.substring(0, name.length() - 1).concat("eвич"));
            } else { // "й"
                if (name.length() > 1) {
                    String last2 = name.substring(name.length() - 2);
                    String pre = name.substring(0, name.length() - 2);
                    switch (last2) {
                        case "ай":
                            list.add(pre.concat("аевич"));
                            break;
                        case "ей":
                            list.add(pre.concat("еевич"));
                            break;
                        case "ий":
                            list.add(pre.concat("ьевич"));
                            break;
                        case "уй":
                            list.add(pre.concat("уевич"));
                            break;
                        default:
                            list.add(pre.concat("евич"));
                            break;
                    }
                } else {
                    list.add("Евич");
                }
            }
        });
        return list;
    }

    private static List<String> getNames(String filename) {
        List<String> list = new ArrayList<>();
        try {
            Files.lines(Paths.get(filename), StandardCharsets.UTF_8).forEach((e) -> {
                if (!e.isEmpty() && e.matches("[а-яА-Я]*")) {
                    list.add(e);
                }
            });
        } catch (IOException ignored) {
        }
        return list;
    }
}
