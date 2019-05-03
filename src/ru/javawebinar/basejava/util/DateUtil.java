package ru.javawebinar.basejava.util;

import java.time.LocalDate;

public class DateUtil {
    public static LocalDate of(int year, int month){
        return LocalDate.of(year, month, 1);
    }
}
