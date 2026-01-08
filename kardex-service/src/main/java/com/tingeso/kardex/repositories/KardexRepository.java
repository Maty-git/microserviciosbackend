package com.tingeso.kardex.repositories;

import com.tingeso.kardex.entities.KardexEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface KardexRepository extends JpaRepository<KardexEntity, Long> {
    List<KardexEntity> findByToolId(Long toolId);
}
