package ru.javawebinar.basejava.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
public class Contact implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String name;
    private final String url;

    private Contact() { // only for marshalling
        this("", "");
    }

    public Contact(String name, String url) {
        this.name = name != null ? name : "";
        this.url = url != null ? url : "";
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

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return name.equals(contact.name)
                && url.equals(contact.url);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }

}

