package com.tingeso.loan.services;

import com.tingeso.loan.dtos.ClientDTO;
import com.tingeso.loan.dtos.ToolDTO;
import com.tingeso.loan.entities.Loan;
import com.tingeso.loan.repositories.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LoanService {

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    org.springframework.web.client.RestTemplate restTemplate;

    public Loan createLoan(String rut, Long toolId) {
        // 1. Validate Client exists
        ClientDTO client = restTemplate.getForObject("http://client-service/api/clients/rut/" + rut, ClientDTO.class);
        if (client == null) {
            throw new RuntimeException("Client not found with RUT: " + rut);
        }

        // 2. Validate Tool exists and is available
        ToolDTO tool = restTemplate.getForObject("http://tool-service/api/tools/" + toolId, ToolDTO.class);
        if (tool == null) {
            throw new RuntimeException("Tool not found with ID: " + toolId);
        }

        // Check availability
        if (!"AVAILABLE".equalsIgnoreCase(tool.getState()) || tool.isOutOfService()) {
            throw new RuntimeException("Tool is not available for loan.");
        }

        // 3. Create Loan
        Loan loan = new Loan();
        loan.setUserRut(rut); // Storing RUT as per Entity definition
        loan.setClientId(client.getId()); // Storing ID
        loan.setToolId(toolId);
        loan.setPrice(tool.getRentDailyRate()); // Example logic
        loan.setQuantity(1);
        loan.setStatus(Loan.LoanStatus.ACTIVE);
        loan.setReturnDateExpected(LocalDateTime.now().plusDays(7)); // Example 7 days
        loan.setDeliveryDate(LocalDateTime.now());

        Loan savedLoan = loanRepository.save(loan);

        // 4. Update Tool status
        restTemplate.put("http://tool-service/api/tools/loan/" + toolId, null);

        // 5. Register in Kardex
        try {
            Map<String, Object> kardexRequest = new HashMap<>();
            kardexRequest.put("toolId", toolId);
            kardexRequest.put("operation", "LOAN");
            kardexRequest.put("userRut", rut);
            kardexRequest.put("date", LocalDateTime.now().toString());
            restTemplate.postForObject("http://kardex-service/api/kardex", kardexRequest, Object.class);
        } catch (Exception e) {
            System.err.println("Error registering in Kardex: " + e.getMessage());
            // Non-blocking error
        }

        return savedLoan;
    }

    public List<Loan> getAllLoans() {
        List<Loan> loans = loanRepository.findAll();
        loans.forEach(this::hydrateLoan);
        return loans;
    }

    public Loan getLoanById(Long id) {
        Loan loan = loanRepository.findById(id).orElse(null);
        if (loan != null) {
            hydrateLoan(loan);
        }
        return loan;
    }

    public List<Loan> getActiveLoans() {
        List<Loan> loans = loanRepository.findByStatus(Loan.LoanStatus.ACTIVE);
        loans.forEach(this::hydrateLoan);
        return loans;
    }

    private void hydrateLoan(Loan loan) {
        try {
            ClientDTO client = restTemplate.getForObject("http://client-service/api/clients/" + loan.getClientId(),
                    ClientDTO.class);
            if (client != null) {
                loan.setClientName(client.getName());
                loan.setClientRut(client.getRut());
            }
            ToolDTO tool = restTemplate.getForObject("http://tool-service/api/tools/" + loan.getToolId(),
                    ToolDTO.class);
            if (tool != null) {
                loan.setToolName(tool.getName());
            }
        } catch (Exception e) {
            System.err.println("Error hydrating loan " + loan.getId() + ": " + e.getMessage());
        }
    }

    public Boolean returnLoan(Long id, String userRut, Boolean withDamage) {
        Loan loan = loanRepository.findById(id).orElse(null);
        if (loan == null)
            return false;

        // 1. Update Loan
        loan.setStatus(Loan.LoanStatus.RETURNED);
        loan.setRealReturnDate(LocalDateTime.now());
        loanRepository.save(loan);

        // 2. Update Tool Status
        // If damaged -> UNDER_REPAIR, else -> AVAILABLE
        // We need an endpoint in ToolService that accepts status update?
        // Or we can just use the generic update or a specific one.
        // Assuming we rely on a PUT to /api/tools/return/{id} or similar, or just
        // update the object.
        // Let's retry fetching the tool to get its prices.
        ToolDTO tool = restTemplate.getForObject("http://tool-service/api/tools/" + loan.getToolId(), ToolDTO.class);

        if (withDamage) {
            restTemplate.put("http://tool-service/api/tools/status/" + loan.getToolId() + "/UNDER_REPAIR", null);
        } else {
            restTemplate.put("http://tool-service/api/tools/status/" + loan.getToolId() + "/AVAILABLE", null);
        }

        // 3. Register Kardex (RETURN)
        try {
            Map<String, Object> kardexRequest = new HashMap<>();
            kardexRequest.put("toolId", loan.getToolId());
            kardexRequest.put("operation", "RETURN");
            kardexRequest.put("userRut", userRut);
            kardexRequest.put("date", LocalDateTime.now().toString());
            restTemplate.postForObject("http://kardex-service/api/kardex", kardexRequest, Object.class);
        } catch (Exception e) {
            System.err.println("Error registering Kardex return: " + e.getMessage());
        }

        // 4. Create Debt if Damaged
        if (withDamage && tool != null) {
            try {
                Map<String, Object> debtRequest = new HashMap<>();
                debtRequest.put("clientId", loan.getClientId());
                debtRequest.put("loanId", loan.getId());
                debtRequest.put("amount", tool.getRepairCost()); // or replacementValue? usually repair for damage
                debtRequest.put("date", LocalDate.now().toString());
                debtRequest.put("dueDate", LocalDate.now().plusDays(30).toString());
                debtRequest.put("status", "UNPAID");
                debtRequest.put("type", "DAMAGE_FEE");

                restTemplate.postForObject("http://debt-service/api/debts", debtRequest, Object.class);
            } catch (Exception e) {
                System.err.println("Error creating Debt: " + e.getMessage());
            }
        }

        return true;
    }

    public List<Object[]> getTopLoanedTools() {
        return loanRepository.findTopLoanedTools();
    }
}
