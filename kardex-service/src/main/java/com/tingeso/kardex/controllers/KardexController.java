package com.tingeso.kardex.controllers;

import com.tingeso.kardex.entities.KardexEntity;
import com.tingeso.kardex.services.KardexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/kardex")
@CrossOrigin("*")
public class KardexController {

    @Autowired
    KardexService kardexService;

    @GetMapping("/tool/{id}")
    public ResponseEntity<List<KardexEntity>> getKardexByToolId(@PathVariable Long id) {
        return ResponseEntity.ok(kardexService.getKardexByToolId(id));
    }

    @PostMapping
    public ResponseEntity<KardexEntity> saveKardex(@RequestBody KardexEntity kardex) {
        return ResponseEntity.ok(kardexService.save(kardex));
    }
}
