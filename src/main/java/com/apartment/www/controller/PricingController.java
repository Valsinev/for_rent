package com.apartment.www.controller;

import com.apartment.www.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Controller
@RequestMapping("/pricing")
public class PricingController {

    private final ReservationService reservationService;

    @Autowired
    public PricingController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public String getReservations(Model model) {
        Map<String, Boolean> reservedDates = reservationService.getReservations();
        Map<YearMonth, List<Integer>> grouped = new TreeMap<>();

        reservedDates.forEach((dateStr, isReserved) -> {
            if (isReserved) {
                LocalDate date = LocalDate.parse(dateStr);
                YearMonth ym = YearMonth.from(date);
                grouped.computeIfAbsent(ym, k -> new ArrayList<>()).add(date.getDayOfMonth());
            }
        });
        model.addAttribute("reservedGrouped", grouped);
        return "pricing_and_availability";
    }
}
