package com.apartment.www.config;

import com.apartment.www.dto.ReservationDto;
import com.apartment.www.entity.Reservation;
import com.apartment.www.entity.ReservationDate;
import lombok.Data;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

@Data
@Configuration
public class AppConfig {

	@Value("${spring.security.user.name}")
	private String adminUsername;


	@Value("${spring.security.user.password}")
	private String adminPassword;

	@Bean
	public ModelMapper getMapper() {
		ModelMapper modelMapper = new ModelMapper();

		Converter<List<ReservationDate>, List<Integer>> dateToDayOfMonthConverter =
				ctx -> ctx.getSource() == null ? null :
						ctx.getSource().stream()
								.map(reservationDate -> reservationDate.getDate().getDayOfMonth())
								.toList();

		Converter<List<Integer>, List<ReservationDate>> dayOfMonthToDateConverter = ctx -> {
			if (ctx == null) return null;
			ReservationDto reservationDto = (ReservationDto) ctx.getParent().getSource();
			int year = reservationDto.getYear();
			int month = reservationDto.getMonth();

			return ctx.getSource().stream()
					.map(day -> {
						LocalDate date = LocalDate.of(year, month, day);
						ReservationDate rd = new ReservationDate();
						rd.setDate(date);
						return rd;
					}).toList();
		};

		modelMapper.typeMap(Reservation.class, ReservationDto.class).addMappings(mapper -> {
			mapper.using(dateToDayOfMonthConverter)
					.map(Reservation::getDates, ReservationDto::setDays);
		});

		modelMapper.typeMap(ReservationDto.class, Reservation.class).addMappings(mapper -> {
			mapper.using(dayOfMonthToDateConverter)
					.map(ReservationDto::getDays, Reservation::setDates);
		});


		return modelMapper;
	}


}
