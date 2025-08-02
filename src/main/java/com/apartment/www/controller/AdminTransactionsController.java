package com.apartment.www.controller;

import com.apartment.www.dto.ExpenseDto;
import com.apartment.www.dto.IncomeDto;
import com.apartment.www.service.ExpenseService;
import com.apartment.www.service.IncomeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

@Controller
@RequestMapping("/admin/transactions")
public class AdminTransactionsController {

    private final IncomeService incomeService;
    private final ExpenseService expenseService;

    @Autowired
	public AdminTransactionsController(IncomeService incomeService, ExpenseService expenseService) {
		this.incomeService = incomeService;
		this.expenseService = expenseService;
	}

	@GetMapping
    public String getTransactionPage(Model model) {

        if (!model.containsAttribute("incomes")) {
            model.addAttribute("incomes", List.of());
        }
        if (!model.containsAttribute("expenses")) {
            model.addAttribute("expenses", List.of());
        }
        if (!model.containsAttribute("totalIncomes")) {
            model.addAttribute("totalIncomes", BigDecimal.ZERO);
        }
        if (!model.containsAttribute("totalExpenses")) {
            model.addAttribute("totalExpenses", BigDecimal.ZERO);
        }
        if (!model.containsAttribute("incomesMinusExpenses")) {
            model.addAttribute("incomesMinusExpenses", BigDecimal.ZERO);
        }
        if (!model.containsAttribute("start")) {
            model.addAttribute("start", LocalDate.now());
        }
        if (!model.containsAttribute("end")) {
            model.addAttribute("end", LocalDate.now());
        }
        if (!model.containsAttribute("isSearched")) {
            model.addAttribute("isSearched", false);
        }

        return "transactions";
    }


    @GetMapping("/income")
    public String getCreateIncomeForm(Model model) {

        if (!model.containsAttribute("incomeDto")) {
            model.addAttribute("incomeDto", new IncomeDto());
        }
        if (!model.containsAttribute("start")) {
            model.addAttribute("start", LocalDate.now());
        }
        if (!model.containsAttribute("end")) {
            model.addAttribute("end", LocalDate.now());
        }
        return "transactions-add-income";
    }


    @GetMapping("/expense")
    public String getCreateExpenseForm(Model model) {

        if (!model.containsAttribute("expenseDto")) {
            model.addAttribute("expenseDto", new IncomeDto());
        }
        if (!model.containsAttribute("start")) {
            model.addAttribute("start", LocalDate.now());
        }
        if (!model.containsAttribute("end")) {
            model.addAttribute("end", LocalDate.now());
        }
        return "transactions-add-expense";
    }

    @PostMapping("/addIncome")
    public String saveIncome(@Valid @ModelAttribute IncomeDto incomeDto,
                             BindingResult bindingResult,
                             @RequestParam("start") LocalDate start,
                             @RequestParam("end") LocalDate end,
                             RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("incomeDto", incomeDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.incomeDto", bindingResult);
            return "redirect:/admin/transactions/search?start=" + start + "&end=" + end;
        }


        incomeService.save(incomeDto);
        return "redirect:/admin/transactions/search?start=" + start + "&end=" + end;

    }

    @PostMapping("/addExpense")
    public String saveExpense(@Valid @ModelAttribute ExpenseDto expenseDto,
                              BindingResult bindingResult,
                              @RequestParam("start") LocalDate start,
                              @RequestParam("end") LocalDate end,
                              RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("expenseDto", expenseDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.incomeDto", bindingResult);
            return "redirect:/admin/transactions/search?start=" + start + "&end=" + end;
        }

        expenseService.save(expenseDto);
        return "redirect:/admin/transactions/search?start=" + start + "&end=" + end;
    }



    @PostMapping("/search")
    public String findIncomesAndExpensesBetweenDate(@RequestParam("start") LocalDate start,
                                                    @RequestParam("end") LocalDate end,
                                                    RedirectAttributes redirectAttributes) {

        List<IncomeDto> incomes = incomeService.findByDateBetweenOrderByDate(start, end);
        List<ExpenseDto> expenses = expenseService.findByDateBetweenOrderByDate(start, end);
        Stream<BigDecimal> incomeAmountStream = incomes.stream().map(IncomeDto::getAmount);
        Stream<BigDecimal> expenseAmountStream = expenses.stream().map(ExpenseDto::getAmount);

        BigDecimal totalIncomes = incomeAmountStream.reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalExpenses = expenseAmountStream.reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal incomesMinusExpenses = totalIncomes.subtract(totalExpenses);
        redirectAttributes.addFlashAttribute("incomes", incomes);
        redirectAttributes.addFlashAttribute("expenses", expenses);
        redirectAttributes.addFlashAttribute("totalIncomes", totalIncomes);
        redirectAttributes.addFlashAttribute("totalExpenses", totalExpenses);
        redirectAttributes.addFlashAttribute("incomesMinusExpenses", incomesMinusExpenses);
        redirectAttributes.addFlashAttribute("start", start);
        redirectAttributes.addFlashAttribute("end", end);
        redirectAttributes.addFlashAttribute("isSearched", true);
        return "redirect:/admin/transactions";
    }

    @GetMapping("/search")
    public String findIncomesAndExpensesBetweenDateForGetMapping(@RequestParam("start") LocalDate start,
                                                    @RequestParam("end") LocalDate end,
                                                    RedirectAttributes redirectAttributes) {

        List<IncomeDto> incomes = incomeService.findByDateBetweenOrderByDate(start, end);
        List<ExpenseDto> expenses = expenseService.findByDateBetweenOrderByDate(start, end);
        Stream<BigDecimal> incomeAmountStream = incomes.stream().map(IncomeDto::getAmount);
        Stream<BigDecimal> expenseAmountStream = expenses.stream().map(ExpenseDto::getAmount);

        BigDecimal totalIncomes = incomeAmountStream.reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalExpenses = expenseAmountStream.reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal incomesMinusExpenses = totalIncomes.subtract(totalExpenses);
        redirectAttributes.addFlashAttribute("incomes", incomes);
        redirectAttributes.addFlashAttribute("expenses", expenses);
        redirectAttributes.addFlashAttribute("totalIncomes", totalIncomes);
        redirectAttributes.addFlashAttribute("totalExpenses", totalExpenses);
        redirectAttributes.addFlashAttribute("incomesMinusExpenses", incomesMinusExpenses);
        redirectAttributes.addFlashAttribute("start", start);
        redirectAttributes.addFlashAttribute("end", end);
        redirectAttributes.addFlashAttribute("isSearched", true);
        return "redirect:/admin/transactions";
    }


    @PostMapping("/income/edit/{id}")
    public String editIncome(@PathVariable Long id,
                             @RequestParam("start") String start,
                             @RequestParam("end") String end,
                             RedirectAttributes redirectAttributes) {

        IncomeDto incomeDto = incomeService.findById(id);
        redirectAttributes.addFlashAttribute("start", start);
        redirectAttributes.addFlashAttribute("end", end);
        redirectAttributes.addFlashAttribute("incomeDto", incomeDto);
        return "redirect:/admin/transactions/income";

    }

    @PostMapping("/expense/edit/{id}")
    public String editExpense(@PathVariable Long id,
                             @RequestParam("start") String start,
                             @RequestParam("end") String end,
                             RedirectAttributes redirectAttributes) {

        ExpenseDto expenseDto = expenseService.findById(id);
        redirectAttributes.addFlashAttribute("start", start);
        redirectAttributes.addFlashAttribute("end", end);
        redirectAttributes.addFlashAttribute("expenseDto", expenseDto);
        return "redirect:/admin/transactions/expense";
    }

    @PostMapping("/income/delete/{id}")
    public String deleteIncome(@PathVariable Long id,
                               @RequestParam("start") LocalDate start,
                               @RequestParam("end") LocalDate end) {

        incomeService.deleteById(id);
        return "redirect:/admin/transactions/search?start=" + start + "&end=" + end;
    }


    @PostMapping("/expense/delete/{id}")
    public String deleteExpense(@PathVariable Long id,
                                @RequestParam("start") String start,
                                @RequestParam("end") String end) {

        expenseService.deleteById(id);
        return "redirect:/admin/transactions/search?start=" + start + "&end=" + end;
    }


}
