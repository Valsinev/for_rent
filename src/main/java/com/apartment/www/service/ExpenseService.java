package com.apartment.www.service;

import com.apartment.www.dto.ExpenseDto;
import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseService {

	void save(@Valid ExpenseDto expenseDto);

	ExpenseDto findById(Long id);

	List<ExpenseDto> findAllByNameContaining(String expenseName);

	BigDecimal findAllAmountByNameContaining(String expenseName);

	List<ExpenseDto> findByDateBetweenOrderByDate(LocalDate start, LocalDate end);

	void deleteById(Long id);

}
