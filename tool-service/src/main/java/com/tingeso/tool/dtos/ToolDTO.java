package com.tingeso.tool.dtos;

public interface ToolDTO {
    String getName();

    String getCategory();

    String getState();

    int getRentDailyRate();

    int getLateFee();

    int getReplacementValue();

    int getCount(); // cantidad agrupada
}
