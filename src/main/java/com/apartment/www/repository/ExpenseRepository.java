package com.apartment.www.repository;

import com.apartment.www.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {


    @Query("SELECT e FROM Expense e WHERE e.date BETWEEN :start AND :end")
    List<Expense> findByDateBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);
}
