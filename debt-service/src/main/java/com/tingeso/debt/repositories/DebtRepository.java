package com.tingeso.debt.repositories;

import com.tingeso.debt.entities.DebtEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DebtRepository extends JpaRepository<DebtEntity, Long> {
    List<DebtEntity> findByStatus(String status);

    List<DebtEntity> findByRutAndStatus(String rut, String status);
}
