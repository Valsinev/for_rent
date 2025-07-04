package com.apartment.www.controller;

import com.apartment.www.entity.Expense;
import com.apartment.www.entity.Income;
import com.apartment.www.repository.ExpenseRepository;
import com.apartment.www.repository.IncomeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

@Controller
@RequestMapping("/admin/stats")
public class StatsController {

    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;

    public StatsController(IncomeRepository incomeRepository, ExpenseRepository expenseRepository) {
        this.incomeRepository = incomeRepository;
        this.expenseRepository = expenseRepository;
    }

    @GetMapping
    public String getStatsPage(Model model) {
        model.addAttribute("incomes", List.of());
        model.addAttribute("isSearched", false);
        return "stats.html";
    }

    @GetMapping("/income")
    public String getAddIncomeForm(Model model) {
        model.addAttribute("income", new Income());
        return "add-income.html";
    }


    @GetMapping("/expense")
    public String getAddExpenseForm(Model model) {
        model.addAttribute("expense", new Expense());
        return "add-expense.html";
    }

    @PostMapping("/addIncome")
    public String saveIncome(@ModelAttribute Income income) {
        incomeRepository.save(income);
        return "redirect:/admin/stats";
    }

    @PostMapping("/addExpense")
    public String saveExpense(@ModelAttribute Expense expense) {
        expenseRepository.save(expense);
        return "redirect:/admin/stats";
    }

    @GetMapping("/income/edit/{id}")
    public String editIncome(@PathVariable Long id, Model model) {

        Income income = incomeRepository.findById(id).orElseThrow(() -> new RuntimeException("Income not found"));
        model.addAttribute("income", income);
        return "add-income";
    }

    @PostMapping("/editIncome/{id}")
    public String updateIncome(@PathVariable Long id, @ModelAttribute Income income) {
        income.setId(id); // Ensure ID is retained
        incomeRepository.save(income);
        return "redirect:/admin/stats";
    }

    @GetMapping("/expense/edit/{id}")
    public String editExpenseForm(@PathVariable Long id, Model model) {
        Expense expense = expenseRepository.findById(id).orElseThrow(() -> new RuntimeException("Expense not found"));
        model.addAttribute("expense", expense);
        return "add-expense"; // Reuse the same form
    }

    @PostMapping("/editExpense/{id}")
    public String updateExpense(@PathVariable Long id, @ModelAttribute Expense expense) {
        expense.setId(id); // Ensure ID is retained
        expenseRepository.save(expense);
        return "redirect:/admin/stats";
    }



    @GetMapping("/income/search")
    public String findIncomesAndExpensesBetweenDate(@RequestParam("start") LocalDate start, @RequestParam("end") LocalDate end, Model model) {
        List<Income> incomes = incomeRepository.findByDateBetweenOrderByDate(start, end);
        List<Expense> expenses = expenseRepository.findByDateBetweenOrderByDate(start, end);
        Stream<BigDecimal> incomeAmountStream = incomes.stream().map(Income::getAmount);
        Stream<BigDecimal> expenseAmountStream = expenses.stream().map(Expense::getAmount);

        BigDecimal totalIncomes = incomeAmountStream.reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalExpenses = expenseAmountStream.reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal incomesMinusExpenses = totalIncomes.subtract(totalExpenses);
        model.addAttribute("incomes", incomes);
        model.addAttribute("expenses", expenses);
        model.addAttribute("totalIncomes", totalIncomes);
        model.addAttribute("totalExpenses", totalExpenses);
        model.addAttribute("incomesMinusExpenses", incomesMinusExpenses);
        model.addAttribute("start", start);
        model.addAttribute("end", end);
        model.addAttribute("isSearched", true);
        return "stats.html";
    }

    @PostMapping("/income/delete/{id}")
    public String deleteIncome(@PathVariable Long id,
                               @RequestParam("start") String start,
                               @RequestParam("end") String end) {
        incomeRepository.deleteById(id);
        return "redirect:/admin/stats/income/search?start=" + start + "&end=" + end;
    }

    @PostMapping("/expense/delete/{id}")
    public String deleteExpense(@PathVariable Long id,
                                @RequestParam("start") String start,
                                @RequestParam("end") String end) {
        expenseRepository.deleteById(id);
        return "redirect:/admin/stats/income/search?start=" + start + "&end=" + end;
    }



}
