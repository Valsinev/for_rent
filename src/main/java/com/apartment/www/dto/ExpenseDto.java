package com.apartment.www.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ExpenseDto {
	private Long id;
	@NotBlank(message = "{expense.must.have.name}")
	private String name;
	@NotBlank(message = "{expense.must.have.description}")
	private String description;
	@NotNull(message = "{expense.must.have.date}")
	private LocalDate date;
	@NotNull(message = "{expense.must.have.amount}")
	@Min(value = 10, message = "{expense.must.be.more.than.10}")
	private BigDecimal amount;
}
