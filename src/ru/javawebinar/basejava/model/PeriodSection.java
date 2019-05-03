package ru.javawebinar.basejava.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class PeriodSection extends Section {
    private final String title;
    private final LocalDate dateFrom;
    private final LocalDate dateTo;
    private final String description;

    public PeriodSection(LocalDate dateFrom, LocalDate dateTo, String title, String description) {
        Objects.requireNonNull(title, "Section title must be non-null");
        Objects.requireNonNull(dateFrom, "DateFrom must be non-null");
        //may be add check to non-empty for {title}
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.title = title;
        this.description = description;
    }

    @Override
    void appendToHtmlStringBuilder(StringBuilder builder) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/Y");
        if (builder.length() != 0) {
            builder.append("\r\n");
        }
        builder.append("Начало: ");
        builder.append(dateFormatter.format(dateFrom));
        builder.append("\r\nОкончание: ");
        if (dateTo != null) {
            builder.append(dateFormatter.format(dateTo));
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
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PeriodSection that = (PeriodSection) o;

        if (!title.equals(that.title)) return false;
        if (!dateFrom.equals(that.dateFrom)) return false;
        if (!Objects.equals(dateTo, that.dateTo)) return false;
        return Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + dateFrom.hashCode();
        result = 31 * result + (dateTo != null ? dateTo.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
