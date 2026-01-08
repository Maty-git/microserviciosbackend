package com.tingeso.loan.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToolDTO {
    private Long id;
    private String name;
    private String category;
    private String state;
    private int quantity;
    private int rentDailyRate;
    private int lateFee;
    private int replacementValue;
    private int repairCost;
    private boolean outOfService;
}
