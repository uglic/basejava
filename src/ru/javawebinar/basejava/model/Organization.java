package ru.javawebinar.basejava.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Organization {
    private final Contact contact;
    private final List<Period> history = new ArrayList<>();

    public Organization(Contact contact, Period... history) {
        Objects.requireNonNull(contact, "Contact must be non-null");
        Objects.requireNonNull(history, "Contact history must be non-null");
        this.contact = contact;
        Collections.addAll(this.history, history);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("---\r\n");
        builder.append(contact);
        for (Period historyRecord : history) {
            builder.append("\r\n");
            builder.append(historyRecord);
        }
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organization that = (Organization) o;
        if (!contact.equals(that.contact)) return false;
        return history.equals(that.history);
    }

    @Override
    public int hashCode() {
        int result = contact.hashCode();
        result = 31 * result + history.hashCode();
        return result;
    }
}
