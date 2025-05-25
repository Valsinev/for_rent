package com.apartment.www.controller;

import com.apartment.www.entity.ReservationRequest;
import com.apartment.www.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class ReservationsController {
    private final ReservationService reservationService;

    @Autowired
    public ReservationsController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    public Map<String, Boolean> getReservations() {
        return reservationService.getReservations();
    }

    @PostMapping("/reservations")
    public Map<String, Boolean> updateReservation(@RequestBody List<ReservationRequest> request) {
        return reservationService.updateReservation(request);
    }

}
