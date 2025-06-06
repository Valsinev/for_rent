package com.apartment.www.mapper;


import com.apartment.www.dto.ReservationForm;
import com.apartment.www.entity.Reservation;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ReservationMapper {
    public Reservation toEntity(ReservationForm reservationForm) {
        Reservation reservation = new Reservation();
        reservation.setId(reservationForm.getId());
        reservation.setColor(reservationForm.getColor());
        reservation.setName(reservationForm.getName());
        reservation.setDescription(reservationForm.getDescription());
        reservation.setMonth(reservationForm.getMonth());
        reservation.setYear(reservationForm.getYear());
        reservation.setDates(reservationForm.getSelectedDays().
                stream()
                .map(day -> LocalDate.of(reservationForm.getYear(), reservationForm.getMonth(), day))
                .toList());
        return reservation;
    }

    public ReservationForm toDto(Reservation reservation) {
        ReservationForm reservationForm = new ReservationForm();
        reservationForm.setId(reservation.getId());
        reservationForm.setColor(reservation.getColor());
        reservationForm.setName(reservation.getName());
        reservationForm.setDescription(reservation.getDescription());
        reservationForm.setMonth(reservation.getMonth());
        reservationForm.setYear(reservation.getYear());
        reservationForm.setSelectedDays(reservation.getDates()
                .stream()
                .map(LocalDate::getDayOfMonth)
                .toList());
        return reservationForm;
    }
}
