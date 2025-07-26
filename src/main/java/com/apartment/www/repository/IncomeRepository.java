package com.apartment.www.repository;

import com.apartment.www.entity.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IncomeRepository extends JpaRepository<Income, Long> {

//    @Query("SELECT i FROM Income i WHERE i.date BETWEEN :start AND :end ORDER BY i.date")
//List<Income> findByDateBetween(@Param("start")LocalDate start, @Param("end") LocalDate end);
    List<Income> findByDateBetweenOrderByDate(LocalDate start, LocalDate end);

	List<Income> findAllByNameContaining(String incomeName);

	@Query("SELECT SUM(i.amount) FROM Income i WHERE LOWER(i.name) LIKE LOWER(CONCAT('%', :incomeName, '%'))")
	BigDecimal findAllAmountByNameContaining(@Param("incomeName") String incomeName);
}
