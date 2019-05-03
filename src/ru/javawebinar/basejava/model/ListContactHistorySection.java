package ru.javawebinar.basejava.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ListContactHistorySection extends Section {
    private final Contact contact;
    private final List<PeriodSection> history = new ArrayList<>();

    public ListContactHistorySection(Contact contact, PeriodSection... history) {
        Objects.requireNonNull(contact, "Contact must be non-null");
        Objects.requireNonNull(history, "Contact history must be non-null");
        //may be add check to non-empty for {history}
        this.contact = contact;
        Collections.addAll(this.history, history);
    }

    @Override
    void appendToHtmlStringBuilder(StringBuilder builder) {
        builder.append("---\r\n");
        builder.append(contact.toString());
        for(PeriodSection item: history){
            item.appendToHtmlStringBuilder(builder);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ListContactHistorySection that = (ListContactHistorySection) o;

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
