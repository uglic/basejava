package ru.javawebinar.basejava.model;

import java.util.ArrayList;
import java.util.List;

public class SectionListed extends Section {
    private List<SectionItem> items = new ArrayList<>();

    public SectionListed(SectionsEnum type, SectionItem... items) {
        setType(type);
        for (SectionItem item : items) {
            this.items.add(item);
        }
    }

    public void addSectionItem(SectionItem item) {
        items.add(item);
    }

    @Override
    protected String getAsHtmlContent() {
        StringBuilder builder = new StringBuilder();
        for (SectionItem item : items) {
            if (builder.length() != 0) {
                builder.append("\r\n");
            }
            builder.append(item.getAsHtml());
        }
        return builder.toString();
    }

    @Override
    public void save() {
        System.out.println("TODO: " + getClass().getName() + ".save()");
    }

    @Override
    public void read() {
        System.out.println("TODO: " + getClass().getName() + ".read()");
    }
}
