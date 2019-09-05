package ru.javawebinar.basejava.util;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.concurrent.ThreadLocalRandom;

public class DateUtil {
    public static final LocalDate NOW = LocalDate.of(3000, 1, 1); //3k problem expected
    public static final String NOW_AS_TEXT = "Сейчас";

    public static LocalDate of(int year, int month) {
        return LocalDate.of(year, month, 1);
    }

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/YYYY");

    public static String to(LocalDate date) {
        if (NOW.equals(date)) {
            return NOW_AS_TEXT;
        } else {
            return DATE_FORMATTER.format(date);
        }
    }

    public static LocalDate parse(String from) {
        LocalDate date;
        try {
            date = LocalDate.parse(from);
        } catch (DateTimeParseException e) {
            date = NOW;
        }
        return date;
    }

    public static LocalDate getRandomBetween(LocalDate from, LocalDate to, ThreadLocalRandom random) {
        LocalDate resultDate = from;
        long months = Period.between(from, to).toTotalMonths();
        if (months > 0) {
            resultDate = resultDate.plusMonths(random.nextLong(0, months + 1));
        }
        if (resultDate.isAfter(to) || resultDate.isEqual(to)) {
            resultDate = to.minusDays(1);
        }
        if (resultDate.isBefore(from)) {
            resultDate = from;
        }
        return resultDate;
    }
}
