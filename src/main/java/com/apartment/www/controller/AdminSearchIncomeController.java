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
@RequestMapping("/admin/incomes")
public class AdminSearchIncomeController {

	private final IncomeService incomeService;

	@Autowired
	public AdminSearchIncomeController(IncomeService incomeService) {
		this.incomeService = incomeService;
	}


	@GetMapping
	public String getIncomesPage(Model model) {

		if (!model.containsAttribute("incomes")) {
			model.addAttribute("incomes", List.of());
		}
		if (!model.containsAttribute("totalIncomes")) {
			model.addAttribute("totalIncomes", BigDecimal.ZERO);
		}
		if (!model.containsAttribute("incomeName")) {
			model.addAttribute("incomeName", "");
		}
		return "transactions-search-income";
	}

	@PostMapping("/addIncome")
	public String saveIncome(@Valid @ModelAttribute IncomeDto incomeDto,
	                         BindingResult bindingResult,
	                         @RequestParam("incomeName") String incomeName,
	                         RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute("incomeDto", incomeDto);
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.incomeDto", bindingResult);
			return "redirect:/admin/incomes/search?incomeName=" + incomeName;
		}


		incomeService.save(incomeDto);
		return "redirect:/admin/incomes/search?incomeName=" + incomeName;

	}

	@GetMapping("/edit")
	public String getEditIncomeForm(Model model) {

		if (!model.containsAttribute("incomeDto")) {
			model.addAttribute("incomeDto", new IncomeDto());
		}
		if (!model.containsAttribute("incomeName")) {
			model.addAttribute("incomeName", "");
		}
		return "incomes-search-edit";
	}

	@PostMapping("/edit/{id}")
	public String editIncome(@PathVariable Long id,
	                         @RequestParam("incomeName") String incomeName,
	                         RedirectAttributes redirectAttributes) {

		IncomeDto incomeDto = incomeService.findById(id);
		redirectAttributes.addFlashAttribute("incomeDto", incomeDto);
		redirectAttributes.addFlashAttribute("incomeName", incomeName);
		return "redirect:/admin/incomes/edit";
	}

	@GetMapping("/search")
	public String searchIncomesByNameGetMapping(@RequestParam("incomeName") String incomeName,
	                                            RedirectAttributes redirectAttributes) {

		List<IncomeDto> allByName = incomeService.findAllByNameContaining(incomeName);

		//summ the amount of all
		BigDecimal totalIncomes = incomeService.findAllAmountByNameContaining(incomeName);

		if (allByName == null || allByName.isEmpty()) {
			redirectAttributes.addFlashAttribute("incomeName", incomeName);
			return "redirect:/admin/incomes";
		}

		redirectAttributes.addFlashAttribute("incomeName", incomeName);
		redirectAttributes.addFlashAttribute("incomes", allByName);
		redirectAttributes.addFlashAttribute("totalIncomes", totalIncomes);

		return "redirect:/admin/incomes";
	}


	@PostMapping("/search")
	public String searchIncomesByName(@RequestParam("incomeName") String incomeName,
	                                  RedirectAttributes redirectAttributes) {



		List<IncomeDto> allByName = incomeService.findAllByNameContaining(incomeName);

		//summ the amount of all
		BigDecimal totalIncomes = incomeService.findAllAmountByNameContaining(incomeName);

		if (allByName == null || allByName.isEmpty() || incomeName.isBlank()) {
			redirectAttributes.addFlashAttribute("incomeName", incomeName);
			return "redirect:/admin/incomes";
		}

		redirectAttributes.addFlashAttribute("incomeName", incomeName);
		redirectAttributes.addFlashAttribute("incomes", allByName);
		redirectAttributes.addFlashAttribute("totalIncomes", totalIncomes);

		return "redirect:/admin/incomes";
	}


	@PostMapping("/delete/{id}")
	public String deleteIncome(@PathVariable Long id,
	                           @RequestParam("incomeName") String incomeName,
	                           RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("incomeName", incomeName);
		incomeService.deleteById(id);
		return "redirect:/admin/incomes";
	}
}
