package ru.javawebinar.basejava.model;

public class SectionItemSimple extends SectionItem {

    public SectionItemSimple(String title) {
        setTitle(title);
    }

    @Override
    public String getAsHtml() {
        StringBuilder builder = new StringBuilder();
        builder.append("- ");
        String title = getTitle();
        if(title != null && !title.isEmpty()) {
            builder.append(title);
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
