package com.apartment.www.service;

import com.apartment.www.entity.Reservation;
import com.apartment.www.entity.ReservationDate;
import com.apartment.www.repository.ReservationDatesRepository;
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
    private final ReservationDatesRepository reservationDatesRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, ReservationDatesRepository reservationDatesRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationDatesRepository = reservationDatesRepository;
    }


    @Transactional
    public void save(Reservation reservation) {
        List<ReservationDate> allByYearAndMonthNative = reservationDatesRepository.findAllByYearAndMonthNative(reservation.getYear(), reservation.getMonth());
        List<LocalDate> allReservedDates = allByYearAndMonthNative.stream().map(ReservationDate::getDate).toList();

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
