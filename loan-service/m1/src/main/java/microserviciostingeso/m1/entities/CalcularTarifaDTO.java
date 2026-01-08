package microserviciostingeso.m1.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalcularTarifaDTO {
    private int personas;
    private String rut;
    private int tipo_reserva;
    private LocalDate fecha;
    private LocalDate cumple;
    private int cumplesrestantes;
}
