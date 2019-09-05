package ru.javawebinar.basejava.generator.loader;

import java.util.ArrayList;
import java.util.List;

public class WomanSurnameModifyingLoader extends AbstractModifyingLoader<String> {
    public WomanSurnameModifyingLoader(Iterable<String> manNames) {
        super(manNames);
    }

    @Override
    protected List<String> load(Iterable<String> manNames) {
        List<String> list = new ArrayList<>();
        manNames.forEach((name) -> {
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
}
