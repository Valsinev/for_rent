package com.apartment.www.service;

import com.apartment.www.dto.ReservationDto;
import com.apartment.www.entity.Reservation;
import com.apartment.www.entity.ReservationDate;
import com.apartment.www.repository.ReservationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements ReservationService {
	private final ReservationRepository reservationRepository;
	private final ModelMapper modelMapper;

	@Autowired
	public ReservationServiceImpl(ReservationRepository reservationRepository, ModelMapper modelMapper) {
		this.reservationRepository = reservationRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public Reservation save(ReservationDto reservationDto) {

		Reservation reservation;

		if (reservationDto.getId() == null) {
			reservation = modelMapper.map(reservationDto, Reservation.class);
			return reservationRepository.save(reservation);
		}

		Reservation founded = reservationRepository.findById(reservationDto.getId())
				.orElseThrow(RuntimeException::new);

		founded.getDates().clear();
		reservationRepository.flush();
		reservation = modelMapper.map(reservationDto, Reservation.class);

		return reservationRepository.save(reservation);

	}

	@Override
	public List<ReservationDto> findAll() {

		//all ordered by year then by month then by ReservationDates LocalDate date
		List<Reservation> allReservations = reservationRepository.findAllSortedByYearMonthAndDate();

		return allReservations.stream().map((element) -> modelMapper.map(element, ReservationDto.class)).collect(Collectors.toList());

	}

	@Override
	public ReservationDto findById(Long id) {
		Reservation reservation = reservationRepository.findById(id).orElseThrow(RuntimeException::new);
		return modelMapper.map(reservation, ReservationDto.class);
	}

	@Override
	public void deleteById(Long id) {

		reservationRepository.deleteById(id);
	}

	@Override
	public Map<String, Map<String, List<Integer>>> buildCalendarData(List<ReservationDto> all, Locale locale) {
		Map<String, Map<String, List<Integer>>> calendarData = new LinkedHashMap<>();

		for (ReservationDto reservation : all) {
			String monthYear = reservation.getMonthYear(locale);
			calendarData.putIfAbsent(monthYear, new LinkedHashMap<>()); // changed here

			Map<String, List<Integer>> dayMap = calendarData.get(monthYear);

			for (Integer day : reservation.getDays()) {
				dayMap.putIfAbsent(reservation.getColor(), new ArrayList<>());
				dayMap.get(reservation.getColor()).add(day);
			}
		}
		return calendarData;
	}


	@Override
	public Map<YearMonth, List<Integer>> buildReservedCalendarData(List<ReservationDto> all) {
		Map<YearMonth, List<Integer>> calendarData = new LinkedHashMap<>();

		for (ReservationDto reservation : all) {
			YearMonth yearMonth = YearMonth.of(reservation.getYear(), reservation.getMonth());

			calendarData.putIfAbsent(yearMonth, new ArrayList<>());

			calendarData.get(yearMonth).addAll(reservation.getDays());
		}
		return calendarData;
	}
}
