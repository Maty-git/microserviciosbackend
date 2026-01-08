package microserviciostingeso.m1.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PeticionM4DTO {
    private LocalDate fecha;
    private LocalDate cumple;
    private int cumplesrestantes;
}
