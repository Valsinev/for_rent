package com.apartment.www.controller;

import com.apartment.www.dto.ReservationDto;
import com.apartment.www.entity.Reservation;
import com.apartment.www.repository.ReservationRepository;
import com.apartment.www.service.ReservationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/pricing")
public class PricingController {

    private final ReservationService reservationService;
    private final ModelMapper modelMapper;

    @Autowired
    public PricingController(ReservationService reservationService, ModelMapper modelMapper) {
	    this.reservationService = reservationService;
	    this.modelMapper = modelMapper;
    }

    @GetMapping
    public String getReservations(Model model, Locale locale) {
        List<ReservationDto> all = reservationService.findAll();


        // Map<String(Reservation.year + Reservation.month),  List<Integer(ReservationDate.date.getDayOfMonth)>>
        // separates all reservations by year month and selected days
        Map<YearMonth, List<Integer>> calendarData = reservationService.buildReservedCalendarData(all);


        model.addAttribute("calendarData", calendarData);
//        model.addAttribute("reservations", all);
        model.addAttribute("locale", locale);
        return "pricing_and_availability";
    }
}
