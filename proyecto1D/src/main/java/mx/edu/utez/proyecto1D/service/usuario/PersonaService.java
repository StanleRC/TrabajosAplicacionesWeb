package mx.edu.utez.proyecto1D.service.usuario;

import jakarta.transaction.Transactional;
import mx.edu.utez.proyecto1D.model.persona.Persona;
import mx.edu.utez.proyecto1D.repository.persona.PersonaRepository;
import mx.edu.utez.proyecto1D.repository.usuario.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonaService {

    private final UsuarioRepository usuarioRepository;
    private final PersonaRepository personaRepository;

    public PersonaService(UsuarioRepository usuarioRepository, PersonaRepository personaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.personaRepository = personaRepository;
    }

    public List<Persona> getAllPersonas() {
        return personaRepository.findAll();
    }

    //Actualizar informacion de persona mediante su id
    @Transactional
    public Persona updatePersona(Long id, Persona payload) {
        Persona persona = personaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada"));
        persona.setNombre(payload.getNombre());
        persona.setPrimerApellido(payload.getPrimerApellido());
        persona.setSegundoApellido(payload.getSegundoApellido());
        persona.setCorreo(payload.getCorreo());
        persona.setCurp(payload.getCurp());
        persona.setFechaNacimiento(payload.getFechaNacimiento());
        return personaRepository.save(persona);
    }
}
