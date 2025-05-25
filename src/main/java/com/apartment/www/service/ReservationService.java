package com.apartment.www.service;

import com.apartment.www.entity.Reservation;
import com.apartment.www.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }


    public void save(Reservation reservation) {
        Optional<Reservation> byDate = reservationRepository.findByDate(reservation.getDate());
        if (byDate.isEmpty()) {
            reservationRepository.save(reservation);
        }
        clearOld();
    }

    public void remove(Reservation reservation) {
        Optional<Reservation> byDate = reservationRepository.findByDate(reservation.getDate());
        byDate.ifPresent(reservationRepository::delete);
    }

    public void clearOld() {
        List<Reservation> allByDateBefore = reservationRepository.findAllByDateBefore(LocalDate.now());
        if (!allByDateBefore.isEmpty()) {
            reservationRepository.deleteAll(allByDateBefore);
        }
    }
}
