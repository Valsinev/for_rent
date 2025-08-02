package com.apartment.www.service;

import com.apartment.www.dto.ExpenseDto;
import com.apartment.www.entity.Expense;
import com.apartment.www.repository.ExpenseRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenseServiceImpl implements ExpenseService {

	private final ExpenseRepository expenseRepository;
	private final ModelMapper modelMapper;

	@Autowired
	public ExpenseServiceImpl(ExpenseRepository expenseRepository, ModelMapper modelMapper) {
		this.expenseRepository = expenseRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public void save(ExpenseDto expenseDto) {
		Expense ex = modelMapper.map(expenseDto, Expense.class);
		expenseRepository.save(ex);
	}

	@Override
	public ExpenseDto findById(Long id) {
		Expense expense = expenseRepository.findById(id)
				.orElseThrow(RuntimeException::new);

		return modelMapper.map(expense, ExpenseDto.class);
	}

	@Override
	public List<ExpenseDto> findAllByNameContaining(String expenseName) {
		List<Expense> allByNameContaining = expenseRepository.findAllByNameContaining(expenseName);
		if (allByNameContaining.isEmpty()) {
			return List.of();
		}
		return allByNameContaining.stream()
				.map(expense -> modelMapper.map(expense, ExpenseDto.class))
				.toList();
	}

	@Override
	public BigDecimal findAllAmountByNameContaining(String expenseName) {
		return expenseRepository.findAllAmountByNameContaining(expenseName);
	}

	@Override
	public List<ExpenseDto> findByDateBetweenOrderByDate(LocalDate start, LocalDate end) {
		List<Expense> byDateBetweenOrderByDate = expenseRepository.findByDateBetweenOrderByDate(start, end);
		return byDateBetweenOrderByDate.stream()
				.map((element) -> modelMapper
						.map(element, ExpenseDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public void deleteById(Long id) {

		expenseRepository.deleteById(id);
	}
}
