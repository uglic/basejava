package ru.javawebinar.basejava.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SectionItemPeriod extends SectionItem {
    private final LocalDate dateFrom;
    private final LocalDate dateTo;
    private final String description;

    public SectionItemPeriod(LocalDate dateFrom, LocalDate dateTo, String title, String description) {
        setTitle(title);
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.description = description;
    }

    @Override
    protected void appendHtmlString(StringBuilder builder) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/Y");
        builder.append("\r\nНачало: ");
        if(dateFrom!=null) {
            builder.append(dateFormatter.format(dateFrom));
        } else {
            builder.append("С начала эры");
        }
        builder.append("\r\nОкончание: ");
        if(dateTo!=null) {
            builder.append(dateFormatter.format(dateTo));
        } else {
            builder.append("Сейчас");
        }
        String title = getTitle();
        if(title != null && !title.isEmpty()) {
            builder.append("\r\n*");
            builder.append(title);
            builder.append("*");
        }
        if(description != null && !description.isEmpty()) {
            builder.append("\r\n");
            builder.append(description);
        }
    }
}
