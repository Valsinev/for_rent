package com.apartment.www.repository;

import com.apartment.www.entity.ReservationDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservationDatesRepository extends JpaRepository<ReservationDate, Long> {
    @Query(value = "SELECT * FROM reservation_date WHERE YEAR(date) = :year AND MONTH(date) = :month", nativeQuery = true)
    List<ReservationDate> findAllByYearAndMonthNative(@Param("year") int year, @Param("month") int month);

}
