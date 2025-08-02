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
@RequestMapping("/admin/expenses")
public class AdminSearchExpenseController {

	private final ExpenseService expenseService;

	@Autowired
	public AdminSearchExpenseController(ExpenseService expenseService) {
		this.expenseService = expenseService;
	}


	@GetMapping
	public String getExpensesPage(Model model) {

		if (!model.containsAttribute("expenses")) {
			model.addAttribute("expenses", List.of());
		}
		if (!model.containsAttribute("totalExpenses")) {
			model.addAttribute("totalExpenses", BigDecimal.ZERO);
		}
		if (!model.containsAttribute("expenseName")) {
			model.addAttribute("expenseName", "");
		}
		return "transactions-search-expense";
	}

	@PostMapping("/addExpense")
	public String saveExpense(@Valid @ModelAttribute ExpenseDto expenseDto,
	                         BindingResult bindingResult,
	                         @RequestParam("expenseName") String expenseName,
	                         RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute("expenseDto", expenseDto);
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.expenseDto", bindingResult);
			return "redirect:/admin/expenses/search?expenseName=" + expenseName;
		}


		expenseService.save(expenseDto);
		return "redirect:/admin/expenses/search?expenseName=" + expenseName;

	}

	@GetMapping("/edit")
	public String getEditExpenseForm(Model model) {

		if (!model.containsAttribute("expenseDto")) {
			model.addAttribute("expenseDto", new ExpenseDto());
		}
		if (!model.containsAttribute("expenseName")) {
			model.addAttribute("expenseName", "");
		}
		return "expenses-search-edit";
	}


	@PostMapping("/edit/{id}")
	public String editExpense(@PathVariable Long id,
	                         @RequestParam("expenseName") String expenseName,
	                         RedirectAttributes redirectAttributes) {

		ExpenseDto expenseDto = expenseService.findById(id);
		redirectAttributes.addFlashAttribute("expenseDto", expenseDto);
		redirectAttributes.addFlashAttribute("expenseName", expenseName);
		return "redirect:/admin/expenses/edit";
	}


	@PostMapping("/search")
	public String searchExpensesByName(@RequestParam("expenseName") String expenseName,
	                                   RedirectAttributes redirectAttributes) {

		List<ExpenseDto> allByName = expenseService.findAllByNameContaining(expenseName);

		//summ the amount of all
		BigDecimal totalExpenses = expenseService.findAllAmountByNameContaining(expenseName);

		if (allByName == null || allByName.isEmpty() || expenseName.isBlank()) {
			redirectAttributes.addFlashAttribute("expenseName", expenseName);
			return "redirect:/admin/expenses";
		}

		redirectAttributes.addFlashAttribute("expenseName", expenseName);
		redirectAttributes.addFlashAttribute("expenses", allByName);
		redirectAttributes.addFlashAttribute("totalExpenses", totalExpenses);

		return "redirect:/admin/expenses";
	}

	@GetMapping("/search")
	public String searchExpensesByNameGetMapping(@RequestParam("expenseName") String expenseName,
	                                   RedirectAttributes redirectAttributes) {

		List<ExpenseDto> allByName = expenseService.findAllByNameContaining(expenseName);

		//summ the amount of all
		BigDecimal totalExpenses = expenseService.findAllAmountByNameContaining(expenseName);

		if (allByName == null || allByName.isEmpty() || expenseName.isBlank()) {
			redirectAttributes.addFlashAttribute("expenseName", expenseName);
			return "redirect:/admin/expenses";
		}

		redirectAttributes.addFlashAttribute("expenseName", expenseName);
		redirectAttributes.addFlashAttribute("expenses", allByName);
		redirectAttributes.addFlashAttribute("totalExpenses", totalExpenses);

		return "redirect:/admin/expenses";
	}


	@PostMapping("/delete/{id}")
	public String deleteExpense(@PathVariable Long id,
	                           @RequestParam("expenseName") String expenseName,
	                           RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("expenseName", expenseName);
		expenseService.deleteById(id);
		return "redirect:/admin/expenses";
	}

}
