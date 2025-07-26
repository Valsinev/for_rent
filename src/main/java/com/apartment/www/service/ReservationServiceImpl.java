package com.apartment.www.service;

import com.apartment.www.dto.ReservationDto;
import com.apartment.www.entity.Reservation;
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
    public List<ReservationDto> findAll() {

        //all ordered by year then by month then by ReservationDates LocalDate date
        List<Reservation> allReservations = reservationRepository.findAllSortedByYearMonthAndDate();
//
//        List<ReservationForm> reservationDtos = allReservations.stream().map(reservationMapper::toDto).toList();
//        List<ReservationForm> sortedReservations = reservationDtos.stream()
//                .sorted(
//                        Comparator.comparing(ReservationForm::getYear)
//                                .thenComparing(ReservationForm::getMonth)
//                                .thenComparing(r -> r.getSelectedDays().stream()
//                                        .min(Integer::compareTo)
//                                        .orElse(Integer.MIN_VALUE))
//                ).toList();
        return allReservations.stream().map((element) -> modelMapper.map(element, ReservationDto.class)).collect(Collectors.toList());

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
    public Reservation save(ReservationDto reservationForm) {

        Reservation reservation = modelMapper.map(reservationForm, Reservation.class);

        return reservationRepository.save(reservation);
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
