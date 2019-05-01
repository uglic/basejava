package ru.javawebinar.basejava.model;

public class SectionSimple extends Section {
    private String content;
    private boolean selected;

    public SectionSimple(SectionsEnum type, String content, boolean selected) {
        setType(type);
        this.content = content;
        this.selected = selected;
    }

    @Override
    public void save() {
        System.out.println("TODO: " + getClass().getName() + ".save()");
    }

    @Override
    public void read() {
        System.out.println("TODO: " + getClass().getName() + ".read()");
    }

    @Override
    protected String getAsHtmlContent() {
        StringBuilder builder = new StringBuilder();
        if (content != null && !content.isEmpty()) {
            if (selected) {
                builder.append("*");
            }
            builder.append(content);
            if (selected) {
                builder.append("*");
            }
        }
        return builder.toString();
    }
}
