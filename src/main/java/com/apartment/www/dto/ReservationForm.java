package com.apartment.www.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Data
public class ReservationForm {
    private Long id;
    private String color;
    private String name;
    private String description;
    private int year;
    private int month;
    private List<Integer> selectedDays;
    private String monthYear;

    public String getMonthYear(Locale locale) {
        LocalDate firstDay = LocalDate.of(year, month, 1);
        return firstDay.format(DateTimeFormatter.ofPattern("MMMM yyyy", locale));
    }

}

