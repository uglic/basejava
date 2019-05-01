package ru.javawebinar.basejava.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SectionItemPeriodContact extends SectionItem {
    LocalDate dateFrom;
    LocalDate dateTo;
    Contact contact;
    String description;

    public SectionItemPeriodContact(String title, LocalDate dateFrom, LocalDate dateTo, Contact contact, String description) {
        setTitle(title);
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.contact = contact;
        this.description = description;
    }

    @Override
    public String getAsHtml() {
        StringBuilder builder = new StringBuilder();
        builder.append("---\r\n");
        builder.append(contact.getAsHtml());
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
            builder.append("\r\n* ");
            builder.append(title);
            builder.append("*");
        }
        if(description != null && !description.isEmpty()) {
            builder.append("\r\n");
            builder.append(description);
        }
        return builder.toString();
    }

    @Override
    public void save() {
        System.out.println("TODO: " + getClass().getName() + ".save()");
    }

    @Override
    public void read() {
        System.out.println("TODO: " + getClass().getName() + ".read()");
    }
}
