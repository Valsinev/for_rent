package com.apartment.www.controller;

import com.apartment.www.entity.Reservation;
import com.apartment.www.entity.ReservationRequest;
import com.apartment.www.repository.ReservationRepository;
import com.apartment.www.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ReservationService reservationService;
    private final ReservationRepository reservationRepository;

    @Autowired
    public AdminController(ReservationService reservationService, ReservationRepository reservationRepository) {
        this.reservationService = reservationService;
        this.reservationRepository = reservationRepository;
    }


    @GetMapping
    public String showAdminPage(Model model, Locale locale) {
        List<Reservation> all = reservationRepository.findAll();
        Map<YearMonth, List<Integer>> grouped = new TreeMap<>();

        all.forEach(reservation -> {
            LocalDate date = reservation.getDate();
            YearMonth ym = YearMonth.from(date);
            grouped.computeIfAbsent(ym, k -> new ArrayList<>()).add(date.getDayOfMonth());
        });

        model.addAttribute("reservedGrouped", grouped);
        model.addAttribute("locale", locale);
        return "admin";
    }


    @GetMapping("/add")
    public String showAddForm() {
        return "admin-add";
    }

    @GetMapping("/remove")
    public String showRemoveForm() {
        return "admin-remove";
    }


    @PostMapping("/reservations")
    public String postReservations(@RequestParam int year,
                                                   @RequestParam int month,
                                                   @RequestParam (name = "days", required = false) List<Integer> days)
    {
        if (days == null || days.isEmpty()) {
            return "redirect:/admin";
        }

        List<Reservation> reservations = new ArrayList<>();
        for (Integer day : days) {
            LocalDate date = LocalDate.of(year, month, day);
            Reservation req = new Reservation();
            req.setDate(date);
            reservations.add(req);
        }

        // Here you would forward to your actual service/repository logic:
        reservations.forEach(reservationService::save);

        // For demonstration: print to console or return summary
        System.out.println("Received reservations: " + reservations);
        return "redirect:/admin";

    }

    @PostMapping("/reservations/remove")
    public String removeReservations(
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam(name = "days", required = false) List<Integer> days
    ) {
        if (days == null || days.isEmpty()) {
            return "redirect:/admin";
        }

        List<Reservation> toRemove = new ArrayList<>();
        for (Integer day : days) {
            LocalDate date = LocalDate.of(year, month, day);
            Reservation req = new Reservation();
            req.setDate(date);
            toRemove.add(req);
        }

        // Here you would remove them from the actual data source
        toRemove.forEach(reservationService::remove);
        System.out.println("Removing reservations: " + toRemove);
        return "redirect:/admin";
    }

}
