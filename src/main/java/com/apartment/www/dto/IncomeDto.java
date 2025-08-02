package com.apartment.www.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class IncomeDto {
	private Long id;
	@NotBlank(message = "{income.must.have.name}")
	private String name;
	@NotBlank(message = "{income.must.have.description}")
	private String description;
	@NotNull(message = "{income.must.have.date}")
	private LocalDate date;
	@NotNull(message = "{income.must.have.amount}")
	@Min(value = 10, message = "{income.must.be.more.than.10}")
	private BigDecimal amount;
}
