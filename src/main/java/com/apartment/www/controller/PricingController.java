package com.apartment.www.controller;

import com.apartment.www.entity.Reservation;
import com.apartment.www.repository.ReservationRepository;
import com.apartment.www.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

@Controller
@RequestMapping("/pricing")
public class PricingController {

    private final ReservationService reservationService;
    private final ReservationRepository reservationRepository;

    @Autowired
    public PricingController(ReservationService reservationService, ReservationRepository reservationRepository) {
        this.reservationService = reservationService;
        this.reservationRepository = reservationRepository;
    }

    @GetMapping
    public String getReservations(Model model, Locale locale) {
        List<Reservation> all = reservationRepository.findAll();
        Map<YearMonth, List<Integer>> grouped = new TreeMap<>();

        all.forEach(reservation -> {
                LocalDate date = reservation.getDate();
                YearMonth ym = YearMonth.from(date);
                grouped.computeIfAbsent(ym, k -> new ArrayList<>()).add(date.getDayOfMonth());
        });

        model.addAttribute("reservedGrouped", grouped);
        model.addAttribute("locale", locale);
        return "pricing_and_availability";
    }
}
