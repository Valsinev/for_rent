package com.apartment.www.controller;

import com.apartment.www.dto.IncomeDto;
import com.apartment.www.service.IncomeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/admin/stats/incomes")
public class AdminSearchIncomeController {

	private final IncomeService incomeService;

	@Autowired
	public AdminSearchIncomeController(IncomeService incomeService) {
		this.incomeService = incomeService;
	}


	@GetMapping
	public String getAddIncomeForm(Model model) {
		if (!model.containsAttribute("incomeDto")) {
			model.addAttribute("incomeDto", new IncomeDto());
		}
		return "add-income.html";
	}


	@PostMapping("/addIncome")
	public String saveIncome(@Valid @ModelAttribute IncomeDto incomeDto,
	                         BindingResult bindingResult,
	                         @RequestParam("start") String start,
	                         @RequestParam("end") String end,
	                         RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute("incomeDto", incomeDto);
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.incomeDto", bindingResult);
			redirectAttributes.addFlashAttribute("start", start);
			redirectAttributes.addFlashAttribute("end", end);
			return "redirect:/admin/stats/incomes";
		}

		redirectAttributes.addFlashAttribute("start", start);
		redirectAttributes.addFlashAttribute("end", end);

		incomeService.save(incomeDto);
		return "redirect:/admin/stats";
	}


	@GetMapping("/edit/{id}")
	public String editIncome(@PathVariable Long id, RedirectAttributes redirectAttributes) {

		IncomeDto incomeDto = incomeService.findById(id);
		redirectAttributes.addFlashAttribute("incomeDto", incomeDto);
		return "redirect:/admin/stats/incomes";
	}


	@GetMapping("/search")
	public String getIncomeSearchPage(Model model) {

		if (!model.containsAttribute("incomes")) {
			model.addAttribute("incomes", List.of());
		}
		if (!model.containsAttribute("totalIncomes")) {
			model.addAttribute("totalIncomes", BigDecimal.ZERO);
		}
		return "stats-search-income";
	}

	@PostMapping("/search")
	public String searchIncomesByName(@RequestParam("incomeName") String incomeName,
	                                  RedirectAttributes redirectAttributes) {

		List<IncomeDto> allByName = incomeService.findAllByNameContaining(incomeName);

		//summ the amount of all
		BigDecimal totalIncomes = incomeService.findAllAmountByNameContaining(incomeName);

		if (allByName == null || allByName.isEmpty()) {
			redirectAttributes.addFlashAttribute("incomeName", incomeName);
			return "redirect:/admin/stats/incomes/search";
		}

		redirectAttributes.addFlashAttribute("incomeName", incomeName);
		redirectAttributes.addFlashAttribute("incomes", allByName);
		redirectAttributes.addFlashAttribute("totalIncomes", totalIncomes);

		return "redirect:/admin/stats/incomes/search";
	}


	@PostMapping("/delete/{id}")
	public String deleteIncome(@PathVariable Long id,
	                           @RequestParam("start") String start,
	                           @RequestParam("end") String end,
	                           RedirectAttributes redirectAttributes) {
		incomeService.deleteById(id);
		redirectAttributes.addFlashAttribute("start", start);
		redirectAttributes.addFlashAttribute("end", end);
		return "redirect:/admin/stats/incomes";
	}
}
