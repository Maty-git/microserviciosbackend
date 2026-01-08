package com.tingeso.kardex.services;

import com.tingeso.kardex.entities.KardexEntity;
import com.tingeso.kardex.repositories.KardexRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class KardexService {
    @Autowired
    KardexRepository kardexRepository;

    public List<KardexEntity> getKardexByToolId(Long toolId) {
        return kardexRepository.findByToolId(toolId);
    }

    public KardexEntity save(KardexEntity kardex) {
        return kardexRepository.save(kardex);
    }
}
