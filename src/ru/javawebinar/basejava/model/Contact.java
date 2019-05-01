package ru.javawebinar.basejava.model;

public class Contact implements Model {
    String name;
    String url;
    ContactTypesEnum type;

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
                builder.append(prefix + ":");
            }
            String iconFileName = type.getIcon().getFilename();
            if (!iconFileName.isEmpty()) {
                builder.append("<" + iconFileName + ">");
            }
        } else {
            builder.append("<no type>");
        }

        if (name != null && url != null) {
            builder.append("[" + name + "](" + url + ")");
        } else if (name != null) {
            builder.append("[" + name + "]");
        } else if (url != null) {
            builder.append("(" + url + ")");
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

