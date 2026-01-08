package com.tingeso.loan.repositories;

import com.tingeso.loan.entities.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByUserRut(String userRut);

    List<Loan> findByClientId(Long clientId);

    List<Loan> findByStatus(Loan.LoanStatus status);

    @org.springframework.data.jpa.repository.Query(value = "SELECT l.tool_id, COUNT(l.id) as count FROM loans l GROUP BY l.tool_id ORDER BY count DESC LIMIT 5", nativeQuery = true)
    List<Object[]> findTopLoanedTools();
}
