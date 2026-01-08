package com.tingeso.debt.services;

import com.tingeso.debt.entities.DebtEntity;
import com.tingeso.debt.repositories.DebtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DebtService {
    @Autowired
    DebtRepository debtRepository;

    public List<DebtEntity> getUnpaidDebts() {
        return debtRepository.findByStatus("UNPAID");
    }

    public List<DebtEntity> getOverdueDebts() {
        // Simple logic: Unpaid and due date is past
        List<DebtEntity> unpaid = debtRepository.findByStatus("UNPAID");
        return unpaid.stream()
                .filter(d -> d.getDueDate().isBefore(LocalDate.now()))
                .collect(Collectors.toList());
    }

    public DebtEntity save(DebtEntity debt) {
        return debtRepository.save(debt);
    }
}
