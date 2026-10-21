package mx.edu.utez.proyecto1D.controller.Persona;

import mx.edu.utez.proyecto1D.model.persona.Persona;
import mx.edu.utez.proyecto1D.service.usuario.PersonaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/persona")
public class PersonaController {
    private final PersonaService personaService;

    public PersonaController(PersonaService personaService) {
        this.personaService = personaService;
    }

    //Obtener todas las personas
    @GetMapping("/mostrarPersonas")
    public ResponseEntity<List<Persona>> getAllPersonas() {
        return ResponseEntity.status(200).body(
                personaService.getAllPersonas()
        );
    }

    //Actualizar informacion de persona
    @PostMapping("/actualizar/{id}")
    public ResponseEntity<Persona> updatePersona(@PathVariable Long id, @RequestBody Persona payload) {
        return ResponseEntity.status(200).body(
                personaService.updatePersona(id, payload)
        );
    }
}
