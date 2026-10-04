package mx.edu.utez.proyecto1D.controller;

import jakarta.validation.Valid;
import mx.edu.utez.proyecto1D.controller.dto.*;
import mx.edu.utez.proyecto1D.service.MyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin({"*"})
@RequestMapping("/myservices")
public class MyController {

    private final MyService myService;
    public MyController(MyService myService) {
        this.myService = myService;
    }


    @PostMapping("/calcularCosto")
    public ResponseEntity<ResponseEnvioDTO> cotizarPaquete(@RequestBody @Valid RequestEnvioDTO payload) {
        ResponseEnvioDTO respuesta = myService.calcularCosto(payload);
        return ResponseEntity.ok(respuesta);
    }

    @PostMapping("/calcularRenta")
    public ResponseEntity<ResponseRentaDTO> cotizarRenta(@RequestBody @Valid RequestRentaDTO payload) {
        return ResponseEntity.ok(myService.calcularRenta(payload));
    }

    @PostMapping("/calcularHospedaje")
    public ResponseEntity<ResponseHospedajeDTO> cotizarHospedaje(@RequestBody @Valid RequestHospedajeDTO payload) {
        return ResponseEntity.ok(myService.calcularHospedaje(payload));
    }
}
