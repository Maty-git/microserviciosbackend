package com.tingeso.debt.controllers;

import com.tingeso.debt.entities.DebtEntity;
import com.tingeso.debt.services.DebtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/debts")
@CrossOrigin("*")
public class DebtController {

    @Autowired
    DebtService debtService;

    @GetMapping("/unpaid")
    public ResponseEntity<List<DebtEntity>> getUnpaidDebts() {
        return ResponseEntity.ok(debtService.getUnpaidDebts());
    }

    @GetMapping("/late")
    public ResponseEntity<List<DebtEntity>> getOverdueDebts() {
        return ResponseEntity.ok(debtService.getOverdueDebts());
    }

    @PostMapping
    public ResponseEntity<DebtEntity> createDebt(@RequestBody DebtEntity debt) {
        return ResponseEntity.ok(debtService.save(debt));
    }
}
