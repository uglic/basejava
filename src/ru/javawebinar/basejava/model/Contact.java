package ru.javawebinar.basejava.model;

public class Contact {
    private final String name;
    private final String url;

    public Contact(String name, String url) {
        this.name = name;
        this.url = url;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (name != null && url != null) {
            builder.append("[");
            builder.append(name);
            builder.append("](");
            builder.append(url);
            builder.append(")");
        } else if (name != null) {
            builder.append(" ");
            builder.append(name);
        } else if (url != null) {
            builder.append("(");
            builder.append(url);
            builder.append(")");
        }
        return builder.toString();
    }
}

