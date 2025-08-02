package com.apartment.www.service;

import com.apartment.www.dto.IncomeDto;
import com.apartment.www.entity.Income;
import com.apartment.www.repository.IncomeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IncomeServiceImpl implements IncomeService {

	private final IncomeRepository incomeRepository;
	private final ModelMapper modelMapper;

	@Autowired
	public IncomeServiceImpl(IncomeRepository incomeRepository, ModelMapper modelMapper) {
		this.incomeRepository = incomeRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public void save(IncomeDto incomeDto) {
		Income income = modelMapper.map(incomeDto, Income.class);
		incomeRepository.save(income);
	}

	@Override
	public IncomeDto findById(Long id) {
		Income byId = incomeRepository.findById(id)
				.orElseThrow(RuntimeException::new);
		return modelMapper.map(byId, IncomeDto.class);
	}

	@Override
	public List<IncomeDto> findAllByNameContaining(String incomeName) {
		List<Income> allByNameContaining = incomeRepository.findAllByNameContaining(incomeName);
		if (allByNameContaining.isEmpty()) {
			return List.of();
		}
		return allByNameContaining.stream()
				.map((element) -> modelMapper.map(element, IncomeDto.class)).collect(Collectors.toList());
	}

	@Override
	public BigDecimal findAllAmountByNameContaining(String incomeName) {
		return incomeRepository.findAllAmountByNameContaining(incomeName);
	}

	@Override
	public List<IncomeDto> findByDateBetweenOrderByDate(LocalDate start, LocalDate end) {
		List<Income> byDateBetweenOrderByDate = incomeRepository.findByDateBetweenOrderByDate(start, end);
		return byDateBetweenOrderByDate.stream()
				.map((element) -> modelMapper
						.map(element, IncomeDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public void deleteById(Long id) {
		incomeRepository.deleteById(id);
	}
}
