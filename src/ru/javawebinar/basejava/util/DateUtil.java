package ru.javawebinar.basejava.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    public static final LocalDate NOW = LocalDate.of(3000, 1, 1); //3k problem expected
    public static LocalDate of(int year, int month){
        return LocalDate.of(year, month, 1);
    }
    public static String to(LocalDate date){
        if(NOW.equals(date)) {
            return "Сейчас";
        } else {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("MM/YYYY");
            return df.format(date);
        }
    }
}
