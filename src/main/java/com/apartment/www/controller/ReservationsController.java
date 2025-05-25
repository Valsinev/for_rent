package com.apartment.www.controller;

import com.apartment.www.entity.Reservation;
import com.apartment.www.entity.ReservationRequest;
import com.apartment.www.repository.ReservationRepository;
import com.apartment.www.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class ReservationsController {
    private final ReservationService reservationService;
    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationsController(ReservationService reservationService, ReservationRepository reservationRepository) {
        this.reservationService = reservationService;
        this.reservationRepository = reservationRepository;
    }

    @GetMapping("/reservations")
    public List<Reservation> getReservations() {
        return reservationRepository.findAll();
    }

    @PostMapping("/reservations")
    public void updateReservation(@RequestBody List<Reservation> request) {
        request.forEach(reservationService::save);
    }

}
