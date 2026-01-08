package com.tingeso.loan.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO {
    private Long id;
    private String rut;
    private String name;
    private String email;
    private String phoneNumber;
    private String status;
}
