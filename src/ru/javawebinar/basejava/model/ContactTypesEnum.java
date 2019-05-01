package ru.javawebinar.basejava.model;

public enum ContactTypesEnum {
    PHONE("Тел.", IconsEnum.NONE),
    SKYPE("Skype", IconsEnum.SKYPE),
    EMAIL("Почта", IconsEnum.EMAIL),
    LINKEDIN("", IconsEnum.LINKEDIN),
    GITHUB("", IconsEnum.GITHUB),
    STACKOVERFLOW("", IconsEnum.STACKOVERFLOW),
    HOMESITE("", IconsEnum.NONE);

    private final String prefix;
    private final IconsEnum icon;

    ContactTypesEnum(String prefix, IconsEnum icon) {
        this.prefix = prefix;
        this.icon = icon;
    }

    public String getPrefix() {
        return prefix;
    }

    public IconsEnum getIcon() {
        return icon;
    }
}
