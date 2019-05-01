package ru.javawebinar.basejava.model;

public class SectionItemSimple extends SectionItem {

    public SectionItemSimple(String title) {
        setTitle(title);
    }

    @Override
    protected void appendHtmlString(StringBuilder builder) {
        builder.append("- ");
        String title = getTitle();
        if(title != null && !title.isEmpty()) {
            builder.append(title);
        }
    }
}
