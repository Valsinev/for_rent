package com.apartment.www.controller;

import com.apartment.www.dto.ReservationDto;
import com.apartment.www.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ReservationService reservationService;

    @Autowired
	public AdminController(ReservationService reservationService) {
		this.reservationService = reservationService;
	}


	@GetMapping
    public String showAdminPage(Model model, Locale locale) {

        List<ReservationDto> all = reservationService.findAll();

        // Map<String(Reservation.year + Reservation.month), Map<String(Reservation.color), List<Integer(ReservationDate.date.getDayOfMonth)>>>
        // separates all reservations by year month and sets the color to the selected days
        Map<String, Map<String, List<Integer>>> calendarData = reservationService.buildCalendarData(all, locale);

        model.addAttribute("reservations", all);
        model.addAttribute("calendarData", calendarData);
        return "admin";
    }


    @PostMapping("/reservations")
    public String postReservations(@ModelAttribute ReservationDto reservationForm) {

        reservationService.save(reservationForm);

        return "redirect:/admin";

    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("reservation", new ReservationDto());
        return "admin-add";
    }

    @GetMapping("/remove")
    public String showRemoveForm() {
        return "admin-remove";
    }


    @GetMapping("/reservations/delete/{id}")
    public String removeReservation(@PathVariable Long id) {
        reservationService.deleteById(id);
        return "redirect:/admin";
    }


    @GetMapping("/reservations/edit/{id}")
    public String showEditReservationForm(@PathVariable Long id, Model model) {
        ReservationDto reservation = reservationService.findById(id);
        model.addAttribute("reservation", reservation);
        return "admin-add";
    }

}
