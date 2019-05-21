package ru.javawebinar.basejava.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    public static final LocalDate NOW = LocalDate.of(3000, 1, 1); //3k problem expected
    public static final String NOW_AS_TEXT = "Сейчас";
    public static LocalDate of(int year, int month){
        return LocalDate.of(year, month, 1);
    }
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/YYYY");

    public static String to(LocalDate date){
        if(NOW.equals(date)) {
            return NOW_AS_TEXT;
        } else {
            return DATE_FORMATTER.format(date);
        }
    }
}
