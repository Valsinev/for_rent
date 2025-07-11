package com.apartment.www.mapper;


import com.apartment.www.dto.ReservationForm;
import com.apartment.www.entity.Reservation;
import com.apartment.www.entity.ReservationDate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

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
        if (reservationForm.getSelectedDays() != null) {
        reservation.setDates(reservationForm.getSelectedDays().stream()
                .map(day -> {
                    ReservationDate reservationDate = new ReservationDate();
//                    reservationDate.setReservation(reservation);
                    reservationDate.setDate(LocalDate.of(reservationForm.getYear(), reservationForm.getMonth(), day));
                    return reservationDate;
                })
                .toList());
        }
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
        if (reservation.getDates() != null) {
        reservationForm.setSelectedDays(
                reservation.getDates()
                .stream()
                .map(reservationDate -> reservationDate.getDate().getDayOfMonth())
                .toList());
        }
        return reservationForm;
    }

    public void updateReservationByForm(Reservation reservation, ReservationForm reservationForm) {
        reservation.setName(reservationForm.getName());
        reservation.setColor(reservationForm.getColor());
        reservation.setDescription(reservationForm.getDescription());
        reservation.setYear(reservationForm.getYear());
        reservation.setMonth(reservationForm.getMonth());

        if (reservationForm.getSelectedDays() != null) {
            List<ReservationDate> newDates = reservationForm.getSelectedDays().stream()
                    .map(day -> {
                        ReservationDate reservationDate = new ReservationDate();
                        reservationDate.setDate(LocalDate.of(reservationForm.getYear(), reservationForm.getMonth(), day));
                        return reservationDate;
                    })
                    .toList();

            reservation.getDates().clear();         // ❗ Important
            reservation.getDates().addAll(newDates); // ✅ Add new dates to the same list
        }
    }

}
