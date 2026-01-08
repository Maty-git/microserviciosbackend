package microserviciostingeso.m1.controllers;

import microserviciostingeso.m1.entities.CalcularTarifaDTO;
import microserviciostingeso.m1.entities.PagoEntity;
import microserviciostingeso.m1.services.TarifasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/m1")
public class TarifasController {

    @Autowired
    private TarifasService tarifasService;

    @PostMapping("/tarifa")
    public PagoEntity getTarifa(@RequestBody CalcularTarifaDTO calcularTarifaDTO){
        return tarifasService.calcularTarifa(calcularTarifaDTO.getPersonas(),
                calcularTarifaDTO.getRut(),
                calcularTarifaDTO.getTipo_reserva(),
                calcularTarifaDTO.getFecha(),
                calcularTarifaDTO.getCumple(),
                calcularTarifaDTO.getCumplesrestantes());
    }
}
