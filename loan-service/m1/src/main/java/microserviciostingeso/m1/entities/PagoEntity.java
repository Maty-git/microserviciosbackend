package microserviciostingeso.m1.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PagoEntity {
    private double tarifa;
    private int descuento;
    private double t_d;
    private double t_iva;
    private int cumpleanero;
}
