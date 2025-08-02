package com.apartment.www.service;

import com.apartment.www.dto.IncomeDto;
import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IncomeService {
	void save(@Valid IncomeDto incomeDto);

	IncomeDto findById(Long id);

	List<IncomeDto> findAllByNameContaining(String incomeName);

	BigDecimal findAllAmountByNameContaining(String incomeName);

	List<IncomeDto> findByDateBetweenOrderByDate(LocalDate start, LocalDate end);

	void deleteById(Long id);

}
