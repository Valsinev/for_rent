package com.apartment.www.repository;


import com.apartment.www.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByYearAndMonth(int year, int month);

	@Query("SELECT r FROM Reservation r JOIN r.dates d ORDER BY r.year ASC, r.month ASC, d.date ASC")
	List<Reservation> findAllSortedByYearMonthAndDate();

//	List<Reservation> findAllOrderByYearAscMonthAscReservationDate_DateAsc();
}
