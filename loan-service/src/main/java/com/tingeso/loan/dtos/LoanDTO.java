package com.tingeso.loan.dtos;

import com.tingeso.loan.entities.Loan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanDTO {
    private Long id;
    private LocalDateTime returnDateExpected;
    private LocalDateTime realReturnDate;
    private int quantity;
    private int price;
    private Loan.LoanStatus status;
    private LocalDateTime deliveryDate;
    private String userRut;
    private Boolean delay;
    private Long clientId;
    private Long toolId;
}
