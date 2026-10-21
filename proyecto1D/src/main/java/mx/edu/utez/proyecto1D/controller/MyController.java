package mx.edu.utez.proyecto1D.controller;

import jakarta.validation.Valid;
import mx.edu.utez.proyecto1D.controller.dto.RequestCalculadoraDTO;
import mx.edu.utez.proyecto1D.controller.dto.ResponseCalculadoraDTO;
import mx.edu.utez.proyecto1D.controller.dto.RrquestBody;
import mx.edu.utez.proyecto1D.service.MyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin({"*"})
@RequestMapping("/myservices")
public class MyController {

    private final MyService myService;

    public MyController(MyService myService) {
        this.myService = myService;
    }


    @GetMapping
    public String miPrimerServicio() {
        System.out.println("Hello world");
        return "Hello world";
    }
    @GetMapping("/Segundo servicio")
    public String miSegundoServicio() {
        return "mi segundo servicio";
    }
    @PostMapping
    public String servicioTres(int a, int b) {
        return Integer.toString(a+b);
    }

    ///
    @GetMapping("/pathvariable/{id}")
    public String patServices(@PathVariable String id) {
        System.out.println("El id es: " + id);
        return "El id es: " + id;
    }
    @PostMapping("/requestbody")
    public ResponseEntity<RrquestBody> requestBody(@RequestBody @Valid RrquestBody paylod) {
        System.out.println(paylod.getNombre());
        System.out.println(paylod.getEdad());
        System.out.println(paylod.getCorrer());
        return ResponseEntity
                .status(201)
                .body(paylod);
    }

    @PostMapping("/calculadora")
    public ResponseEntity<ResponseCalculadoraDTO> calculadora
            (@RequestBody @Valid RequestCalculadoraDTO payload){

        return ResponseEntity.status(200).body(myService.calculadora(payload));
    }

}
