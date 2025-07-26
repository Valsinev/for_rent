package com.apartment.www.service;

import com.apartment.www.dto.ReservationDto;
import com.apartment.www.entity.Reservation;

import java.time.YearMonth;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public interface ReservationService {
	List<ReservationDto> findAll();

	Map<String, Map<String, List<Integer>>> buildCalendarData(List<ReservationDto> all, Locale locale);

	Reservation save(ReservationDto reservationForm);

	ReservationDto findById(Long id);

	void deleteById(Long id);

	Map<YearMonth, List<Integer>> buildReservedCalendarData(List<ReservationDto> all);
}
