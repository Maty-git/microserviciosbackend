package com.tingeso.tool.dtos;

import com.tingeso.tool.entities.Tool;

public interface ToolDTOnoKardex {
    Long getId();

    String getName();

    Tool.ToolCategory getCategory();

    Tool.ToolState getState();

    int getQuantity();

    int getRentDailyRate();

    int getLateFee();

    int getReplacementValue();

    int getRepairCost();

    boolean isOutOfService();
}
