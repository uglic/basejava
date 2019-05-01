package ru.javawebinar.basejava.model;

public class Contact implements Model {
    private final String name;
    private final String url;
    private final ContactTypesEnum type;

    public Contact(ContactTypesEnum type, String name, String url) {
        this.type = type;
        this.name = name;
        this.url = url;
    }

    @Override
    public String getAsHtml() {
        StringBuilder builder = new StringBuilder();
        if (type != null) {
            String prefix = type.getPrefix();
            if (!prefix.isEmpty()) {
                builder.append(prefix);
                builder.append(":");
            }
            String iconFileName = type.getIcon().getFilename();
            if (!iconFileName.isEmpty()) {
                builder.append("[");
                builder.append(iconFileName);
                builder.append("]");
            }
        } else {
            builder.append("[no type]");
        }

        if (name != null && url != null) {
            builder.append("[" + name + "](" + url + ")");
        } else if (name != null) {
            builder.append(" " + name + "");
        } else if (url != null) {
            builder.append("(" + url + ")");
        }

        builder.append("\r\n");
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

