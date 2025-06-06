package com.apartment.www.service;

import com.apartment.www.entity.Reservation;
import com.apartment.www.repository.ReservationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }


    @Transactional
    public void save(Reservation reservation) {
        List<Reservation> allByYearAndMonth = reservationRepository.findByYearAndMonth(reservation.getYear(), reservation.getMonth());
        List<LocalDate> allReservedDates = allByYearAndMonth.stream()
                .flatMap(reserved -> reserved.getDates().stream())
                .toList();

        //check if there is dublicate dates for reservation
        reservation.getDates().forEach(reserved -> {
            if (allReservedDates.contains(reserved)) {
                throw new RuntimeException("There is reservation with this date.");
            }
        });

        reservationRepository.save(reservation);
    }

    @Transactional
    public void remove(Reservation reservation) {
        Optional<Reservation> byDate = reservationRepository.findById(reservation.getId());
        byDate.ifPresent(reservationRepository::delete);
    }

}
