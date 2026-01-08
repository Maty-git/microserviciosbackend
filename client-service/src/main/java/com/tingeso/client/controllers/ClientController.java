package com.tingeso.client.controllers;

import com.tingeso.client.dtos.ClientResponseDTO;
import com.tingeso.client.entities.Client;
import com.tingeso.client.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@CrossOrigin("*")
public class ClientController {
    @Autowired
    private ClientService clientService;

    @PostMapping("/save")
    public ResponseEntity<Client> saveClient(@RequestBody Client client) {
        Client clientNew = clientService.saveClient(client);
        return ResponseEntity.ok(clientNew);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ClientResponseDTO>> listClients() {
        List<ClientResponseDTO> clients = clientService.getClients();
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/rutsClients")
    public ResponseEntity<List<String>> listClientsRut() {
        List<String> ruts = clientService.getAllRuts();
        return ResponseEntity.ok(ruts);
    }

    @PutMapping("/update")
    public ResponseEntity<Client> updateClient(@RequestBody Client client) {
        return ResponseEntity.ok(clientService.updateClient(client));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteClient(@PathVariable Long id) throws Exception {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        Client client = clientService.getClientById(id);
        if (client == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(client);
    }

    @GetMapping("/rut/{rut}")
    public ResponseEntity<Client> getClientByRut(@PathVariable String rut) {
        Client client = clientService.getClientByRut(rut);
        if (client == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(client);
    }
}
