package ru.javawebinar.basejava.model;

public class SectionSimple extends Section {
    private final String content;
    private final boolean selected;

    public SectionSimple(SectionsEnum type, String content, boolean selected) {
        setType(type);
        this.content = content;
        this.selected = selected;
    }

    @Override
    protected void appendHtmlString(StringBuilder builder) {
        if (content != null && !content.isEmpty()) {
            if (selected) {
                builder.append("**");
            }
            builder.append(content);
            if (selected) {
                builder.append("**");
            }
        }
    }
}
