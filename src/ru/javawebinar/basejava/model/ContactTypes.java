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

    public String getPrefix() {
        return prefix;
    }

    public IconTypes icon() {
        return icon;
    }
}
