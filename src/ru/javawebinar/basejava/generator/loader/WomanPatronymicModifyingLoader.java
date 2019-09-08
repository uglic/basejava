package ru.javawebinar.basejava.generator.loader;
/*
 * @author Stepan Shcherbakov /uglic.ru/ 2019
 */

import java.util.ArrayList;
import java.util.List;

public class WomanPatronymicModifyingLoader extends AbstractModifyingLoader<String> {
    public WomanPatronymicModifyingLoader(Iterable<String> manNames) {
        super(manNames);
    }

    @Override
    protected List<String> load(Iterable<String> manNames) {
        List<String> list = new ArrayList<>();
        manNames.forEach((name) -> {
            String lastChar = name.substring(name.length() - 1);
            String last2 = name.substring(name.length() - 2);
            if ("ау".contains(lastChar)) {
                list.add(name + "вна");
            } else if ("оыэю".contains(lastChar)) {
                list.add(name + "новна");
            } else if ("бвгдзклмнпрстфхцчшщ".contains(lastChar)) {
                list.add(name + "овна");
            } else if ("жиеёъ".contains(lastChar)) {
                list.add(name + "eвна");
            } else if ("ь".contains(lastChar)) {
                list.add(name.substring(0, name.length() - 1).concat("eвна"));
            } else if ("я".contains(lastChar)) {
                list.add(name.substring(0, name.length() - 1) + "ична");
            } else { // "й"
                if (name.length() > 1) {
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
}
