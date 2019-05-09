package ru.javawebinar.basejava.model;

import java.util.Objects;

public enum ContactTypes {
    PHONE("Тел.", IconTypes.NONE),
    SKYPE("Skype", IconTypes.SKYPE),
    EMAIL("Почта", IconTypes.EMAIL),
    LINKEDIN("", IconTypes.LINKEDIN),
    GITHUB("", IconTypes.GITHUB),
    STACKOVERFLOW("", IconTypes.STACKOVERFLOW),
    HOMESITE("", IconTypes.NONE);

    private final String prefix;
    private final IconTypes icon;

    ContactTypes(String prefix, IconTypes icon) {
        Objects.requireNonNull(prefix);
        Objects.requireNonNull(icon);
        this.prefix = prefix;
        this.icon = icon;
    }

//    @Override
//    public String toString() {
//        StringBuilder builder = new StringBuilder();
//        if (!prefix.isEmpty()) {
//            builder.append(prefix);
//            builder.append(":");
//        }
//        String iconFileName = icon.getFilename();
//        if (!iconFileName.isEmpty()) {
//            builder.append("[");
//            builder.append(iconFileName);
//            builder.append("]");
//        }
//        return builder.toString();
//    }
}
