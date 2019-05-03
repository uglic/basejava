package ru.javawebinar.basejava.model;

import java.util.Objects;

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
            builder.append(name);
        } else if (url != null) {
            builder.append("(");
            builder.append(url);
            builder.append(")");
        }
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        if(!Objects.equals(name, contact.name)) return false;
        return  Objects.equals(url, contact.url);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }

}

