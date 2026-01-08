package com.tingeso.loan.controllers;

import com.tingeso.loan.entities.Loan;
import com.tingeso.loan.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/loans")
@CrossOrigin("*")
public class LoanController {

    @Autowired
    LoanService loanService;

    @GetMapping("/all")
    public ResponseEntity<List<Loan>> getAllLoans() {
        return ResponseEntity.ok(loanService.getAllLoans());
    }

    @PostMapping("/save")
    public ResponseEntity<Loan> createLoan(@RequestBody Map<String, Object> payload) {
        String rut = (String) payload.get("clientRut");
        Long toolId = ((Number) payload.get("toolId")).longValue();

        Loan loan = loanService.createLoan(rut, toolId);
        return ResponseEntity.ok(loan);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Loan> getLoanById(@PathVariable Long id) {
        Loan loan = loanService.getLoanById(id);
        if (loan == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(loan);
    }

    @GetMapping("/active")
    public ResponseEntity<List<Loan>> getActiveLoans() {
        return ResponseEntity.ok(loanService.getActiveLoans());
    }

    @GetMapping("/ranking")
    public ResponseEntity<List<Object[]>> getTopLoanedTools() {
        return ResponseEntity.ok(loanService.getTopLoanedTools());
    }

    @PutMapping("/return/{id}/{user}/{bool}")
    public ResponseEntity<Boolean> returnLoan(@PathVariable Long id, @PathVariable String user,
            @PathVariable Boolean bool) {
        return ResponseEntity.ok(loanService.returnLoan(id, user, bool));
    }
}
