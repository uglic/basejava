package ru.javawebinar.basejava.model;

import ru.javawebinar.basejava.util.DateUtil;
import ru.javawebinar.basejava.util.LocalDateAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class Organization implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Contact contact;
    private final List<Position> history = new ArrayList<>();

    private Organization() { // only for marshalling
        this(new Contact("", ""));
    }

    public Organization(Contact contact, Position... history) {
        Objects.requireNonNull(contact, "Contact must be non-null");
        Objects.requireNonNull(history, "Contact history must be non-null");
        this.contact = contact;
        Collections.addAll(this.history, history);
    }

    public Organization(Contact contact, List<Position> history) {
        Objects.requireNonNull(contact, "Contact must be non-null");
        Objects.requireNonNull(history, "Contact history must be non-null");
        this.contact = contact;
        this.history.addAll(history);
    }

    public Contact getContact() {
        return contact;
    }

    public List<Position> getHistory() {
        return history;
    }

    @Override
    public String toString() {
        return "Organization(" + contact.getUrl() + "," + history + ')';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organization that = (Organization) o;
        return contact.equals(that.contact)
                && history.equals(that.history);
    }

    @Override
    public int hashCode() {
        int result = contact.hashCode();
        result = 31 * result + history.hashCode();
        return result;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Position implements Serializable {
        private static final long serialVersionUID = 1L;

        private final String title;
        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        private final LocalDate startDate;
        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        private final LocalDate endDate;
        private final String description;

        private Position() {// only for marshalling
            this(DateUtil.NOW, DateUtil.NOW, "", "");
        }

        public Position(LocalDate startDate, LocalDate endDate, String title, String description) {
            Objects.requireNonNull(title, "Section title must be non-null");
            Objects.requireNonNull(startDate, "DateFrom must be non-null");
            Objects.requireNonNull(endDate, "DateFrom must be non-null");
            this.startDate = startDate;
            this.endDate = endDate;
            this.title = title;
            this.description = description != null ? description : "";
        }

        public LocalDate getStartDate() {
            return startDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return "Position(" + startDate + ',' + endDate + ',' + title + ',' + description + ')';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Position that = (Position) o;
            return title.equals(that.title)
                    && startDate.equals(that.startDate)
                    && endDate.equals(that.endDate)
                    && description.equals(that.description);
        }

        @Override
        public int hashCode() {
            int result = title.hashCode();
            result = 31 * result + startDate.hashCode();
            result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
            result = 31 * result + (description != null ? description.hashCode() : 0);
            return result;
        }
    }
}
