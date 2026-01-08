package com.tingeso.tool.services;

import com.tingeso.tool.dtos.ToolDTO;
import com.tingeso.tool.dtos.ToolDTOnoKardex;
import com.tingeso.tool.entities.Tool;
import com.tingeso.tool.repositories.ToolRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToolService {
    @Autowired
    ToolRepository toolRepository;

    // Decoupled: Removed external repositories for Loan, Debt, Client, Kardex.
    // TODO: Implement communication via RestTemplate/Feign if needed for business
    // logic.

    public boolean saveTool(Tool tool, String rutUser) {
        int quantity = tool.getQuantity();
        int i;

        for (i = 0; i < quantity; i++) {
            Tool newTool = new Tool();
            newTool.setName(tool.getName());
            newTool.setCategory(tool.getCategory());
            newTool.setState(tool.getState());
            newTool.setQuantity(1);
            newTool.setRentDailyRate(tool.getRentDailyRate());
            newTool.setLateFee(tool.getLateFee());
            newTool.setReplacementValue(tool.getReplacementValue());
            newTool.setRepairCost(tool.getRepairCost());

            // Guardamos primero la herramienta
            Tool savedTool = toolRepository.save(newTool);

            // TODO: call Kardex Service (CREATION)
        }

        return i == quantity;
    }

    public List<ToolDTO> getAllTools() {
        return toolRepository.getToolSummary();
    }

    public List<ToolDTOnoKardex> getAllToolsNoKardex() {
        return toolRepository.getAllToolsNoKardex();
    }

    public Tool getToolById(Long id) {
        return toolRepository.findById(id).get();
    }

    @Transactional
    public void deleteToolById(Long id, String rutUser) {
        Tool tool = toolRepository.findById(id).orElseThrow();

        // 1. Marcar herramienta fuera de servicio
        tool.setOutOfService(true);
        tool.setState(Tool.ToolState.OUT_OF_SERVICE);

        // TODO: call Kardex Service (CANCELLATION)

        toolRepository.save(tool);

        // TODO: Check Loan status via Loan Service and create Debt via Debt Service if
        // needed.
    }

    @Transactional
    public void repairTool(Long id, String rutUser) {
        Tool tool = toolRepository.findById(id).orElseThrow();

        // 1. Restaurar herramienta como disponible
        tool.setOutOfService(false);
        tool.setState(Tool.ToolState.AVAILABLE);

        // TODO: call Kardex Service (REPAIR)

        toolRepository.save(tool);

        // TODO: Check Loan status and create Repair Debt if needed.
    }

    @Transactional
    public void repairToolNoDebt(Long id, String rutUser) {
        Tool tool = toolRepository.findById(id).orElseThrow();

        // 1. Restaurar herramienta como disponible
        tool.setOutOfService(false);
        tool.setState(Tool.ToolState.AVAILABLE);

        // TODO: call Kardex Service (REPAIR)

        toolRepository.save(tool);
    }

    public List<ToolDTOnoKardex> getToolsForRepair() {
        return toolRepository.getToolsForRepair();
    }

    public void setToolToLoaned(Long id) {
        Tool tool = toolRepository.findById(id).orElseThrow();
        tool.setState(Tool.ToolState.LOANED);
        toolRepository.save(tool);
    }

    public void updateToolStatus(Long id, String status) {
        Tool tool = toolRepository.findById(id).orElseThrow();
        tool.setState(Tool.ToolState.valueOf(status.toUpperCase()));
        if ("AVAILABLE".equalsIgnoreCase(status)) {
            tool.setOutOfService(false);
            // reset userRut if needed, but tool entity doesn't store userRut directly?
            // Tool entity doesn't seem to store current holder.
        } else if ("UNDER_REPAIR".equalsIgnoreCase(status) || "OUT_OF_SERVICE".equalsIgnoreCase(status)) {
            tool.setOutOfService(true);
        }
        toolRepository.save(tool);
    }

    @Transactional
    public void updateToolByIdAndGroup(Long id, String rutUser, Tool updatedTool) {
        Tool baseTool = toolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tool not found"));

        // 1. Buscar todas las herramientas que tengan mismos atributos que la base
        // (menos el id)
        List<Tool> toolsToUpdate = toolRepository.findByNameAndCategoryAndRentDailyRateAndLateFeeAndReplacementValue(
                baseTool.getName(),
                baseTool.getCategory(),
                baseTool.getRentDailyRate(),
                baseTool.getLateFee(),
                baseTool.getReplacementValue());

        // 2. Actualizar cada herramienta
        for (Tool t : toolsToUpdate) {
            t.setName(updatedTool.getName());
            t.setCategory(updatedTool.getCategory());
            t.setState(updatedTool.getState());
            t.setRentDailyRate(updatedTool.getRentDailyRate());
            t.setLateFee(updatedTool.getLateFee());
            t.setReplacementValue(updatedTool.getReplacementValue());
            t.setRepairCost(updatedTool.getRepairCost());
            t.setOutOfService(updatedTool.isOutOfService());

            // TODO: call Kardex Service (UPDATE)
        }
        toolRepository.saveAll(toolsToUpdate);
    }

    public ToolDTOnoKardex getToolByNameAndCategory(String toolName, String category) {
        Tool.ToolCategory toolCategory = Tool.ToolCategory.valueOf(category.toUpperCase());
        List<ToolDTOnoKardex> listTool = toolRepository.findOneByNameAndCategory(toolName, toolCategory);
        for (ToolDTOnoKardex tool : listTool) {
            return tool;
        }
        return null;
    }

}
