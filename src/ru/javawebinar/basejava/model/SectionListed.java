package ru.javawebinar.basejava.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SectionListed extends Section {
    private final List<SectionItem> items = new ArrayList<>();

    public SectionListed(SectionsEnum type, SectionItem... items) {
        setType(type);
        Collections.addAll(this.items, items);
    }

    @Override
    protected void appendHtmlString(StringBuilder builder) {
        for (SectionItem item : items) {
            if (builder.length() != 0) {
                builder.append("\r\n");
            }
            item.appendHtmlString(builder);
        }
    }
}
