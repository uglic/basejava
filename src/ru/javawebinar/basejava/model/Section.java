package ru.javawebinar.basejava.model;

public abstract class Section implements Model {
    private SectionsEnum type;

    protected final void setType(SectionsEnum type) {
        this.type = type;
    }

    protected final SectionsEnum getType() {
        return type;
    }

    @Override
    public String getAsHtml() {
        StringBuilder builder = new StringBuilder();
        String title = getType().getTitle();
        if(!title.isEmpty()){
            builder.append("# " + title);
            builder.append("\r\n");
        }
        builder.append(getAsHtmlContent());
        builder.append("\r\n");
        return builder.toString();
    }

    protected abstract String getAsHtmlContent();
}