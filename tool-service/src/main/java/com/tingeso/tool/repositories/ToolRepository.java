package com.tingeso.tool.repositories;

import com.tingeso.tool.dtos.ToolDTO;
import com.tingeso.tool.dtos.ToolDTOnoKardex;
import com.tingeso.tool.entities.Tool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToolRepository extends JpaRepository<Tool, Long> {
    @Query("SELECT t.name AS name, " +
            "t.category AS category, " +
            "t.state AS state, " +
            "t.rentDailyRate AS rentDailyRate, " +
            "t.lateFee AS lateFee, " +
            "t.replacementValue AS replacementValue, " +
            "COUNT(t) AS count " +
            "FROM Tool t " +
            "WHERE t.state <> com.tingeso.tool.entities.Tool.ToolState.OUT_OF_SERVICE " +
            "GROUP BY t.name, t.category, t.state, t.rentDailyRate, t.lateFee, t.replacementValue")
    List<ToolDTO> getToolSummary();

    @Query("SELECT t.id AS id, " +
            "t.name AS name, " +
            "t.category AS category, " +
            "t.state AS state, " +
            "t.quantity AS quantity, " +
            "t.rentDailyRate AS rentDailyRate, " +
            "t.lateFee AS lateFee, " +
            "t.replacementValue AS replacementValue, " +
            "t.repairCost AS repairCost, " +
            "t.outOfService AS outOfService " +
            "FROM Tool t " +
            "WHERE t.state <> com.tingeso.tool.entities.Tool.ToolState.OUT_OF_SERVICE")
    List<ToolDTOnoKardex> getAllToolsNoKardex();

    @Query("SELECT t.id AS id, " +
            "t.name AS name, " +
            "t.category AS category, " +
            "t.state AS state, " +
            "t.quantity AS quantity, " +
            "t.rentDailyRate AS rentDailyRate, " +
            "t.lateFee AS lateFee, " +
            "t.replacementValue AS replacementValue, " +
            "t.repairCost AS repairCost, " +
            "t.outOfService AS outOfService " +
            "FROM Tool t " +
            "WHERE t.state = com.tingeso.tool.entities.Tool.ToolState.UNDER_REPAIR")
    List<ToolDTOnoKardex> getToolsForRepair();

    List<Tool> findByNameAndCategoryAndRentDailyRateAndLateFeeAndReplacementValue(
            String name,
            Tool.ToolCategory category,
            int rentDailyRate,
            int lateFee,
            int replacementValue);

    @Query("SELECT t.id AS id, " +
            "t.name AS name, " +
            "t.category AS category, " +
            "t.state AS state, " +
            "t.quantity AS quantity, " +
            "t.rentDailyRate AS rentDailyRate, " +
            "t.lateFee AS lateFee, " +
            "t.replacementValue AS replacementValue, " +
            "t.repairCost AS repairCost, " +
            "t.outOfService AS outOfService " +
            "FROM Tool t " +
            "WHERE t.name = :name AND t.category = :category")
    List<ToolDTOnoKardex> findOneByNameAndCategory(@Param("name") String name,
            @Param("category") Tool.ToolCategory category);
}
