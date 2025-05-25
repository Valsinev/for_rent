package com.apartment.www.repository;


import com.apartment.www.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findByDate(LocalDate date);

    List<Reservation> findAllByDateBefore(LocalDate date);
}
