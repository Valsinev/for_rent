package com.apartment.www.controller;

import com.apartment.www.dto.ReservationForm;
import com.apartment.www.entity.Reservation;
import com.apartment.www.mapper.ReservationMapper;
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
    private final ReservationMapper reservationMapper;

    @Autowired
    public PricingController(ReservationService reservationService, ReservationRepository reservationRepository, ReservationMapper reservationMapper) {
        this.reservationService = reservationService;
        this.reservationRepository = reservationRepository;
        this.reservationMapper = reservationMapper;
    }

    @GetMapping
    public String getReservations(Model model, Locale locale) {
        List<Reservation> all = reservationRepository.findAll();
        List<ReservationForm> dtos = all.stream().map(reservationMapper::toDto).toList();

        // Build calendarData: Map<MonthYear String, Map<Day Integer, List<Color>>>
        Map<YearMonth, List<Integer>> calendarData = new LinkedHashMap<>();

        for (ReservationForm reservation : dtos) {
            YearMonth yearMonth = YearMonth.of(reservation.getYear(), reservation.getMonth());

            calendarData.putIfAbsent(yearMonth, new ArrayList<>());

            calendarData.get(yearMonth).addAll(reservation.getSelectedDays());
        }

        model.addAttribute("calendarData", calendarData);
//        model.addAttribute("reservations", all);
        model.addAttribute("locale", locale);
        return "pricing_and_availability";
    }
}
