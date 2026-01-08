package com.tingeso.client.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponseDTO {
    private String rut;
    private String name;
    private String email;
    private String phoneNumber;
    private String status;
}
