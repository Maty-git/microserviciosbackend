package com.tingeso.client.repositories;

import com.tingeso.client.dtos.ClientDTO;
import com.tingeso.client.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findByRut(String rut);

    @Query("SELECT c.rut FROM Client c")
    List<String> findAllRuts();

    @Query("SELECT c.id AS id, " +
            "c.name AS name, " +
            "c.rut AS rut, " +
            "c.email AS email, " +
            "c.status AS status " +
            "FROM Client c " +
            "WHERE c.rut = :rut")
    ClientDTO findClientDTOByRut(@Param("rut") String rut);
}
