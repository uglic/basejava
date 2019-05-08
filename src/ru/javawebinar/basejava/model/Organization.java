package ru.javawebinar.basejava.model;

import ru.javawebinar.basejava.util.DateUtil;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Organization implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Contact contact;
    private final List<Period> history = new ArrayList<>();

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
        if (!contact.equals(that.contact)) return false;
        return history.equals(that.history);
    }

    @Override
    public int hashCode() {
        int result = contact.hashCode();
        result = 31 * result + history.hashCode();
        return result;
    }

    public static class Period implements Serializable {
        private static final long serialVersionUID = 1L;

        private final String title;
        private final LocalDate startDate;
        private final LocalDate endDate;
        private final String description;

        public Period(LocalDate startDate, LocalDate endDate, String title, String description) {
            Objects.requireNonNull(title, "Section title must be non-null");
            Objects.requireNonNull(startDate, "DateFrom must be non-null");
            Objects.requireNonNull(endDate, "DateFrom must be non-null");
            this.startDate = startDate;
            this.endDate = endDate;
            this.title = title;
            this.description = description;
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
            if (!title.equals(that.title)) return false;
            if (!startDate.equals(that.startDate)) return false;
            if (!Objects.equals(endDate, that.endDate)) return false;
            return Objects.equals(description, that.description);
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
