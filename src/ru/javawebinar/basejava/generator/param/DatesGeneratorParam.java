package ru.javawebinar.basejava.generator.param;

import java.time.LocalDate;

public class DatesGeneratorParam implements IGeneratorParameter {
    private final boolean isMan;
    private final LocalDate dateFrom;
    private final LocalDate dateTo;

    public DatesGeneratorParam(boolean isMan, LocalDate dateFrom, LocalDate dateTo) {
        this.isMan = isMan;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    @Override
    public Boolean isMan() {
        return isMan;
    }

    @Override
    public LocalDate getDateFrom() {
        return dateFrom;
    }

    @Override
    public LocalDate getDateTo() {
        return dateTo;
    }
}
