package ru.javawebinar.basejava.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SectionItemContactPeriod extends SectionItem {
    private final Contact contact;
    private final List<SectionItemPeriod> history = new ArrayList<>();

    public SectionItemContactPeriod(Contact contact, SectionItemPeriod ... history) {
        this.contact = contact;
        Collections.addAll(this.history, history);
    }

    @Override
    protected void appendHtmlString(StringBuilder builder) {
        builder.append("---\r\n");
        builder.append(contact.getAsHtml());
        for(SectionItemPeriod item: history){
            item.appendHtmlString(builder);
        }
    }
}
