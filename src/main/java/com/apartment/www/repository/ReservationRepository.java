package com.apartment.www.repository;


import com.apartment.www.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByYearAndMonth(int year, int month);
}
