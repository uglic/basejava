package ru.javawebinar.basejava.generator.loader;
/*
 * @author Stepan Shcherbakov /uglic.ru/ 2019
 */

import java.util.ArrayList;
import java.util.List;

public class ManPatronymicModifyingLoader extends AbstractModifyingLoader<String> {
    public ManPatronymicModifyingLoader(Iterable<String> manNames) {
        super(manNames);
    }

    @Override
    protected List<String> load(Iterable<String> manNames) {
        List<String> list = new ArrayList<>();
        manNames.forEach((name) -> {
            String lastChar = name.substring(name.length() - 1);
            String last2 = name.substring(name.length() - 2);
            if ("ау".contains(lastChar)) {
                list.add(name + "вич");
            } else if ("оыэю".contains(lastChar)) {
                list.add(name + "нович");
            } else if ("бвгдзклмнпрстфхцчшщ".contains(lastChar)) {
                list.add(name + "ович");
            } else if ("жиеёъ".contains(lastChar)) {
                list.add(name + "eвич");
            } else if ("ь".contains(lastChar)) {
                list.add(name.substring(0, name.length() - 1).concat("eвич"));
            } else if ("я".contains(lastChar)) {
                switch (last2) {
                    case "ья":
                        list.add(name.substring(0, name.length() - 1) + "ич");
                    default:
                        list.add(name + "нович");
                }
            } else { // "й"
                if (name.length() > 1) {
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
}
