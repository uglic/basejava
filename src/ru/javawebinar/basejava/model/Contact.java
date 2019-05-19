package ru.javawebinar.basejava.model;

import ru.javawebinar.basejava.exception.StorageException;

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

    public Contact(String name, ContactTypes type) {
        this.name = name != null ? name : "";
        if (!this.name.isEmpty()) {
            switch (type) {
                case PHONE:
                    url = name;
                    break;
                case SKYPE:
                    url = "skype:" + name;
                    break;
                case EMAIL:
                    url = "mailto:" + name;
                    break;
                case LINKEDIN:
                case GITHUB:
                case STACKOVERFLOW:
                case HOMESITE:
                    url = name;
                    break;
                default:
                    throw new StorageException("Unknown contact type: " + type);
            }
        } else {
            this.url = "";
        }
    }

    @Override
    public String toString() {
        return "Contact(" + name + ',' + url + ')';
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

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String toHtml() {
        if (url.isEmpty()) {
            return name;
        } else {
            return toLink(name, url);
        }
    }

    public String toHtml(ContactTypes type) {
        switch (type) {
            //case PHONE:
            case SKYPE:
            case EMAIL:
                return toLink(name, url);
            case LINKEDIN:
            case GITHUB:
            case STACKOVERFLOW:
            case HOMESITE:
                return toLink(type.getPrefix(), url);
            default:
                return name;
        }
    }

    public String toHtmlView(ContactTypes type) {
        switch (type) {
            case PHONE:
            case SKYPE:
            case EMAIL:
                return type.getPrefix() + ": " + toHtml(type);
            default:
                return toHtml(type);
        }
    }

    private String toLink(String title, String href) {
        return "<a href='" + href + "'>" + title + "</a>";
    }
}
