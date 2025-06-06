package com.apartment.www.controller;

import com.apartment.www.dto.ReservationForm;
import com.apartment.www.entity.Reservation;
import com.apartment.www.entity.ReservationDate;
import com.apartment.www.entity.ReservationRequest;
import com.apartment.www.mapper.ReservationMapper;
import com.apartment.www.repository.ReservationDatesRepository;
import com.apartment.www.repository.ReservationRepository;
import com.apartment.www.service.ReservationService;
import jakarta.transaction.Transactional;
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
    private final ReservationDatesRepository reservationDatesRepository;

    @Autowired
    public AdminController(ReservationService reservationService, ReservationRepository reservationRepository, ReservationMapper reservationMapper, ReservationDatesRepository reservationDatesRepository) {
        this.reservationService = reservationService;
        this.reservationRepository = reservationRepository;
        this.reservationMapper = reservationMapper;
        this.reservationDatesRepository = reservationDatesRepository;
    }


    @GetMapping
    public String showAdminPage(Model model, Locale locale) {
        List<Reservation> allReservations = reservationRepository.findAll();

        List<ReservationForm> dtos = allReservations.stream().map(reservationMapper::toDto).toList();

        model.addAttribute("reservations", dtos);

        // Build calendarData: Map<MonthYear String, Map<Day Integer, List<Color>>>
        Map<String, Map<String, List<Integer>>> calendarData = new LinkedHashMap<>();

        for (ReservationForm reservation : dtos) {
            String monthYear = reservation.getMonthYear(locale);
            calendarData.putIfAbsent(monthYear, new TreeMap<>()); // Sorted days

            Map<String, List<Integer>> dayMap = calendarData.get(monthYear);

            for (Integer day : reservation.getSelectedDays()) {
                dayMap.putIfAbsent(reservation.getColor(), new ArrayList<>());
                dayMap.get(reservation.getColor()).add(day);
            }
        }

        model.addAttribute("calendarData", calendarData);
        return "admin";
    }


    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("reservation", new ReservationForm());
        return "admin-add";
    }

    @GetMapping("/remove")
    public String showRemoveForm() {
        return "admin-remove";
    }


    @PostMapping("/reservations")
    @Transactional
    public String postReservations(@ModelAttribute ReservationForm reservationForm)
    {
        Reservation reservation = reservationMapper.toEntity(reservationForm);


        reservationRepository.save(reservation);

        return "redirect:/admin";

    }

    @GetMapping("/reservations/delete/{id}")
    public String removeReservation(@PathVariable Long id) {
        reservationRepository.deleteById(id);
        return "redirect:/admin";
    }


    @GetMapping("/reservations/edit/{id}")
    public String showEditReservationForm(@PathVariable Long id, Model model) {
        Optional<Reservation> reservationOpt = reservationRepository.findById(id);
        if (reservationOpt.isEmpty()) {
            return "redirect:/admin"; // or show an error page
        }

        ReservationForm reservationForm = reservationMapper.toDto(reservationOpt.get());
        model.addAttribute("reservation", reservationForm);
        return "admin-add";
    }

}
