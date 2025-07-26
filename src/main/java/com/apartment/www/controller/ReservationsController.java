package com.apartment.www.controller;

import com.apartment.www.dto.ReservationDto;
import com.apartment.www.entity.Reservation;
import com.apartment.www.repository.ReservationRepository;
import com.apartment.www.service.ReservationService;
import com.apartment.www.service.ReservationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReservationsController {
    private final ReservationService reservationService;

	public ReservationsController(ReservationService reservationService) {
		this.reservationService = reservationService;
	}


	@GetMapping("/reservations")
    public List<ReservationDto> getReservations() {
        return reservationService.findAll();
    }

    @PostMapping("/reservations")
    public void updateReservation(@RequestBody List<ReservationDto> request) {
        request.forEach(reservationService::save);
    }

}
