package ru.javawebinar.basejava.generator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class FullNameGenerator {
    private final static String MEN_NAMES = "config/men_names.txt";
    private final static String WOMEN_NAMES = "config/women_names.txt";
    private final static String MEN_SURNAMES = "config/men_surnames.txt";

    private static volatile FullNameGenerator instance;

    private final LazyList<String> manNames = new LazyList<>(FullNameGenerator::loadMenNames);
    private final LazyList<String> manPatronymics = new LazyList<>(() -> loadMenPatronymics(manNames));
    private final LazyList<String> manSurnames = new LazyList<>(FullNameGenerator::loadMenSurnames);
    private final LazyList<String> womanNames = new LazyList<>(FullNameGenerator::loadWomenNames);
    private final LazyList<String> womanPatronymics = new LazyList<>(() -> loadWomenPatronymics(manNames));
    private final LazyList<String> womanSurnames = new LazyList<>(() -> loadWomenSurnames(manSurnames));

    private FullNameGenerator() {
    }

    public static FullNameGenerator getInstance() {
        if (instance == null) {
            synchronized (FullNameGenerator.class) {
                if (instance == null) {
                    instance = new FullNameGenerator();
                }
            }
        }
        return instance;
    }

    public String getRandomFullName(boolean isMen) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        String name;
        if (isMen) {
            return getRandomFromList(manSurnames, random)
                    .concat(" " + getRandomFromList(manNames, random)
                            .concat(" " + getRandomFromList(manPatronymics, random)));
        } else {
            return getRandomFromList(womanSurnames, random)
                    .concat(" " + getRandomFromList(womanNames, random)
                            .concat(" " + getRandomFromList(womanPatronymics, random)));
        }
    }

    private String getRandomFromList(LazyList<String> lazyList, ThreadLocalRandom random) {
        String name = lazyList.get(random.nextInt(lazyList.size()));
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }

    private static List<String> loadMenNames() {
        List<String> list = getNames(MEN_NAMES);
        if (list.isEmpty()) {
            list.add("Александр");
            list.add("Иван");
            list.add("Олег");
            list.add("Петр");
        }
        return list;
    }

    private static List<String> loadWomenNames() {
        List<String> list = getNames(WOMEN_NAMES);
        if (list.isEmpty()) {
            list.add("Анна");
            list.add("Василиса");
            list.add("Наталья");
            list.add("Ольга");
        }
        return list;
    }

    private static List<String> loadMenSurnames() {
        List<String> list = getNames(MEN_SURNAMES);
        if (list.isEmpty()) {
            list.add("Простофилин");
            list.add("Трудолюбов");
            list.add("Владимиров");
            list.add("Ласточкин");
        }
        return list;
    }

    private static List<String> loadWomenSurnames(Iterable<String> manSurnames) {
        List<String> list = new ArrayList<>();
        manSurnames.forEach((name) -> {
            String last2 = name.substring(name.length() - 2);
            switch (last2) {
                case "ев":
                case "ов":
                case "ын":
                case "ин":
                    list.add(name.concat("а"));
                    break;
                case "ий":
                case "ый":
                    String pre2 = name.substring(0, name.length() - 2);
                    if (last2.equals("ий")) {
                        switch (name.charAt(name.length() - 3)) {
                            case 'н':
                            case 'р':
                                list.add(pre2.concat("яя"));
                                break;
                            default:
                                list.add(pre2.concat("ая"));
                        }
                    } else {
                        list.add(pre2.concat("ая"));
                    }
                    break;
                default:
                    list.add(name);
            }
        });
        return list;
    }

    private static List<String> loadMenPatronymics(Iterable<String> manNames) {
        List<String> list = new ArrayList<>();
        manNames.forEach((name) -> {
            String lastChar = name.substring(name.length() - 1);
            if ("ау".contains(lastChar)) {
                list.add(name + "вич");
            } else if ("оыэюя".contains(lastChar)) {
                list.add(name + "нович");
            } else if ("бвгдзклмнпрстфхцчшщ".contains(lastChar)) {
                list.add(name + "ович");
            } else if ("жиеёъ".contains(lastChar)) {
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

    private static List<String> loadWomenPatronymics(Iterable<String> manNames) {
        List<String> list = new ArrayList<>();
        manNames.forEach((name) -> {
            String lastChar = name.substring(name.length() - 1);
            if ("ау".contains(lastChar)) {
                list.add(name + "вна");
            } else if ("оыэюя".contains(lastChar)) {
                list.add(name + "новна");
            } else if ("бвгдзклмнпрстфхцчшщ".contains(lastChar)) {
                list.add(name + "овна");
            } else if ("жиеёъ".contains(lastChar)) {
                list.add(name + "eвна");
            } else if ("ь".contains(lastChar)) {
                list.add(name.substring(0, name.length() - 1).concat("eвна"));
            } else { // "й"
                if (name.length() > 1) {
                    String last2 = name.substring(name.length() - 2);
                    String pre = name.substring(0, name.length() - 2);
                    switch (last2) {
                        case "ай":
                            list.add(pre.concat("аевна"));
                            break;
                        case "ей":
                            list.add(pre.concat("еевна"));
                            break;
                        case "ий":
                            list.add(pre.concat("ьевна"));
                            break;
                        case "уй":
                            list.add(pre.concat("уевна"));
                            break;
                        default:
                            list.add(pre.concat("евна"));
                            break;
                    }
                } else {
                    list.add("Евна");
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
