package ru.javawebinar.basejava.model;

public abstract class Section implements Model {
    private SectionsEnum type;

    final void setType(SectionsEnum type) {
        this.type = type;
    }

    final SectionsEnum getType() {
        return type;
    }

    @Override
    public String getAsHtml() {
        StringBuilder builder = new StringBuilder();
        String title = getType().getTitle();
        if(!title.isEmpty()){
            builder.append("# ");
            builder.append(title);
            builder.append("\r\n");
        }
        appendHtmlString(builder);
        builder.append("\r\n");
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