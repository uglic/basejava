package ru.javawebinar.basejava.model;

import ru.javawebinar.basejava.util.DateUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Period {
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
