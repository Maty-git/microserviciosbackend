package microserviciostingeso.m1.services;

import microserviciostingeso.m1.entities.DescuentoDTO;
import microserviciostingeso.m1.entities.PagoEntity;
import microserviciostingeso.m1.entities.PaqueteM4DTO;
import microserviciostingeso.m1.entities.PeticionM4DTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;

@Service
public class TarifasService {

    @Autowired
    private WebClient.Builder webClientBuilder;
    private final int IVA = 119;

    public PagoEntity calcularTarifa(int personas, String rut, int tipo_reserva, LocalDate fecha, LocalDate cumple, int cumplesrestantes){
        PagoEntity pago = new PagoEntity();
        if (tipo_reserva ==1){
            pago.setTarifa(15000);
        }else if (tipo_reserva ==2){
            pago.setTarifa(20000);
        }else {
            pago.setTarifa(25000);
        }
        System.out.println("cumple: " + cumple);
        //llamar a m2, m3 y m4
        int m2 = obtenerDescuentoM2(personas);
        int m3 = obtenerDescuentoM3(rut);
        PaqueteM4DTO m4 = obtenerDescuentoM4(fecha, cumple, cumplesrestantes);

        pago.setDescuento(Math.max(Math.max(m2, m3), m4.getDescuento()));
        if (Math.max(Math.max(m2, m3), m4.getDescuento()) == m4.getDescuento()
            && m4.getCumpleanero() == 1){
            pago.setCumpleanero(1);
        }else {
            pago.setCumpleanero(0);
        }
        if (pago.getDescuento() > 0){
            pago.setT_d( ((pago.getTarifa() * (100 - pago.getDescuento())) / 100));
        }else {
            pago.setT_d(pago.getTarifa());
        }
        pago.setT_iva( (pago.getT_d() * IVA) / 100 );
        return pago;
    }
    private int obtenerDescuentoM2(int personas){

        DescuentoDTO m2;
        try {
             m2 = webClientBuilder.build()
                    .get()
                    .uri("http://m2/m2/descuento/{personas}", personas)
                    .retrieve()
                    .bodyToMono(DescuentoDTO.class)
                    .block();
            assert m2 != null;
            return m2.getDescuento();
        } catch (Exception e) {
            System.err.println("Error llamando a M2 con WebClient: " + e.getMessage());
        }
        return -1;
    }
    private int obtenerDescuentoM3(String rut){
        DescuentoDTO m3;
        try {
            m3 = webClientBuilder.build()
                    .post()
                    .uri("http://m3/descuento/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(rut)
                    .retrieve()
                    .bodyToMono(DescuentoDTO.class)
                    .block();
            assert m3 != null;
            return m3.getDescuento();
        } catch (Exception e) {
            System.err.println("Error llamando a M3 con WebClient: " + e.getMessage());
        }
        return -1;
    }
    private PaqueteM4DTO obtenerDescuentoM4(LocalDate fecha, LocalDate cumple, int cumplesrestantes){

        PaqueteM4DTO m4 = new PaqueteM4DTO();
        PeticionM4DTO peticion = new PeticionM4DTO();
        peticion.setFecha(fecha);
        peticion.setCumple(cumple);
        peticion.setCumplesrestantes(cumplesrestantes);
        try {
            m4 = webClientBuilder.build()
                    .post()
                    .uri("http://m4/m4/descuento")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(peticion)
                    .retrieve()
                    .bodyToMono(PaqueteM4DTO.class)
                    .block();
            assert m4 != null;
            return m4;
        } catch (Exception e) {
            System.err.println("Error llamando a M4 con WebClient: " + e.getMessage());
        }
        m4.setDescuento(-1);
        m4.setCumpleanero(0);
        return m4;
    }
}
