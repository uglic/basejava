package ru.javawebinar.basejava.model;

import ru.javawebinar.basejava.util.DateUtil;
import ru.javawebinar.basejava.util.LocalDateAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class Organization implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Contact contact;
    private final List<Period> history = new ArrayList<>();

    private Organization() { // only for marshalling
        this(new Contact("", ""));
    }

    public Organization(Contact contact, Period... history) {
        Objects.requireNonNull(contact, "Contact must be non-null");
        Objects.requireNonNull(history, "Contact history must be non-null");
        this.contact = contact;
        Collections.addAll(this.history, history);
    }

    public Organization(Contact contact, List<Period> history) {
        Objects.requireNonNull(contact, "Contact must be non-null");
        Objects.requireNonNull(history, "Contact history must be non-null");
        this.contact = contact;
        this.history.addAll(history);
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
    public static class Period implements Serializable {
        private static final long serialVersionUID = 1L;

        private final String title;
        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        private final LocalDate startDate;
        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        private final LocalDate endDate;
        private final String description;

        private Period() {// only for marshalling
            this(DateUtil.NOW, DateUtil.NOW, "", "");
        }

        public Period(LocalDate startDate, LocalDate endDate, String title, String description) {
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
            StringBuilder builder = new StringBuilder();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/Y");
            builder.append("Начало: ");
            builder.append(dateFormatter.format(startDate));
            builder.append("\r\nОкончание: ");
            if (endDate != DateUtil.NOW) {
                builder.append(dateFormatter.format(endDate));
            } else {
                builder.append("Сейчас");
            }
            if (!title.isEmpty()) {
                builder.append("\r\n*");
                builder.append(title);
                builder.append("*");
            }
            if (description != null && !description.isEmpty()) {
                builder.append("\r\n");
                builder.append(description);
            }
            return builder.toString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Period that = (Period) o;

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
