package ru.javawebinar.basejava.model;

public abstract class SectionItem implements Model {
    private String title;

    final void setTitle(String title) {
        this.title = title;
    }

    final String getTitle() {
        return title;
    }

    @Override
    public String getAsHtml() {
        StringBuilder builder = new StringBuilder();
        appendHtmlString(builder);
        return builder.toString();
    }

    protected abstract void appendHtmlString(StringBuilder builder);

    @Override
    public void save() {
        System.out.println("TODO: " + getClass().getName() + ".save()");
    }

    @Override
    public void read() {
        System.out.println("TODO: " + getClass().getName() + ".read()");
    }
}
