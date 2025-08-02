package com.apartment.www.controller;

import com.apartment.www.dto.ExpenseDto;
import com.apartment.www.service.ExpenseService;
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
@RequestMapping("/admin/stats/expenses")
public class AdminSearchExpenseController {

	private final ExpenseService expenseService;

	@Autowired
	public AdminSearchExpenseController(ExpenseService expenseService) {
		this.expenseService = expenseService;
	}


	@GetMapping
	public String getAddExpenseForm(Model model) {
		if (!model.containsAttribute("expenseDto")) {
			model.addAttribute("expenseDto", new ExpenseDto());

		}
		return "add-expense.html";
	}


	@PostMapping("/addExpense")
	public String saveExpense(@Valid @ModelAttribute ExpenseDto expenseDto,
	                          BindingResult bindingResult,
	                          RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute("expenseDto", expenseDto);
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.expenseDto", bindingResult);
			return "redirect:/admin/stats/expenses";
		}
		expenseService.save(expenseDto);
		return "redirect:/admin/stats";
	}


	@GetMapping("/edit/{id}")
	public String editExpenseForm(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		ExpenseDto expenseDto = expenseService.findById(id);
		redirectAttributes.addFlashAttribute("expenseDto", expenseDto);
		return "redirect:/admin/stats/expenses";
	}

	@GetMapping("/search")
	public String getExpenseSearchPage(Model model) {

		if (!model.containsAttribute("expenses")) {
			model.addAttribute("expenses", List.of());
		}
		if (!model.containsAttribute("totalExpenses")) {
			model.addAttribute("totalExpenses", BigDecimal.ZERO);
		}
		return "stats-search-expense";
	}

	@PostMapping("/search")
	public String searchExpensesByName(@RequestParam("expenseName") String expenseName,
	                                   RedirectAttributes redirectAttributes) {

		List<ExpenseDto> allByName = expenseService.findAllByNameContaining(expenseName);

		//summ the amount of all
		BigDecimal totalExpenses = expenseService.findAllAmountByNameContaining(expenseName);

		if (allByName == null || allByName.isEmpty()) {
			redirectAttributes.addFlashAttribute("expenseName", expenseName);
			return "redirect:/admin/stats/expenses/search";
		}

		redirectAttributes.addFlashAttribute("expenseName", expenseName);
		redirectAttributes.addFlashAttribute("expenses", allByName);
		redirectAttributes.addFlashAttribute("totalExpenses", totalExpenses);

		return "redirect:/admin/stats/expenses/search";
	}


	@PostMapping("/delete/{id}")
	public String deleteExpense(@PathVariable Long id,
	                            @RequestParam("start") String start,
	                            @RequestParam("end") String end,
	                            RedirectAttributes redirectAttributes) {
		expenseService.deleteById(id);
		redirectAttributes.addFlashAttribute("start", start);
		redirectAttributes.addFlashAttribute("end", end);
		return "redirect:/admin/stats/expenses";
	}

}
