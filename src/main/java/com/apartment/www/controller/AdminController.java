package com.apartment.www.controller;

import com.apartment.www.dto.ReservationForm;
import com.apartment.www.entity.Reservation;
import com.apartment.www.entity.ReservationRequest;
import com.apartment.www.mapper.ReservationMapper;
import com.apartment.www.repository.ReservationRepository;
import com.apartment.www.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ReservationService reservationService;
    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;

    @Autowired
    public AdminController(ReservationService reservationService, ReservationRepository reservationRepository, ReservationMapper reservationMapper) {
        this.reservationService = reservationService;
        this.reservationRepository = reservationRepository;
        this.reservationMapper = reservationMapper;
    }


    @GetMapping
    public String showAdminPage(Model model, Locale locale) {
        List<Reservation> allReservations = reservationRepository.findAll();

        List<ReservationForm> dtos = allReservations.stream().map(reservationMapper::toDto).toList();

        model.addAttribute("reservations", dtos);

        // Build calendarData: Map<MonthYear String, Map<Day Integer, List<Color>>>
        Map<String, Map<Integer, List<String>>> calendarData = new LinkedHashMap<>();

        for (ReservationForm reservation : dtos) {
            String monthYear = reservation.getMonthYear(locale);
            calendarData.putIfAbsent(monthYear, new TreeMap<>()); // Sorted days

            Map<Integer, List<String>> dayMap = calendarData.get(monthYear);

            for (Integer day : reservation.getSelectedDays()) {
                dayMap.putIfAbsent(day, new ArrayList<>());
                dayMap.get(day).add(reservation.getColor());
            }
        }

        model.addAttribute("calendarData", calendarData);
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
    public String postReservations(@ModelAttribute ReservationForm reservationForm)
    {
        Reservation reservation = reservationMapper.toEntity(reservationForm);
        List<Reservation> allByYarAndMonth = reservationRepository.findByYearAndMonth(reservation.getYear(), reservation.getMonth());
        List<LocalDate> allDates = allByYarAndMonth.stream().flatMap(reserved -> reserved.getDates().stream()).toList();

        //if there is dublicate date by another reservation
        reservation.getDates().forEach(day -> {
            if (allDates.contains(day)) {
                throw new RuntimeException("There is another reservation on this day.");
            }
        });

        reservationRepository.save(reservation);

        // For demonstration: print to console or return summary
        System.out.println("Received reservations: " + reservation);
        return "redirect:/admin";

    }

    @GetMapping("/reservations/delete/{id}")
    public String removeReservation(@PathVariable Long id) {
        reservationRepository.deleteById(id);
        return "redirect:/admin";
    }

}
