package mx.edu.utez.proyecto1D.service.usuario;

import jakarta.transaction.Transactional;
import mx.edu.utez.proyecto1D.controller.usuario.dto.RequestCreateUserDTO;
import mx.edu.utez.proyecto1D.controller.usuario.dto.RequestUpdatePersona;
import mx.edu.utez.proyecto1D.exception.customExceptions.CustomBadRequestException;
import mx.edu.utez.proyecto1D.model.persona.Persona;
import mx.edu.utez.proyecto1D.model.usuario.Roles;
import mx.edu.utez.proyecto1D.model.usuario.Usuario;
import mx.edu.utez.proyecto1D.repository.persona.PersonaRepository;
import mx.edu.utez.proyecto1D.repository.usuario.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {
    //Inyeccion de dependencias
    private final UsuarioRepository usuarioRepository;
    private final PersonaRepository personaRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, PersonaRepository personaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.personaRepository = personaRepository;
    }

    //servicios
    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    @Transactional
    public Usuario createUser(RequestCreateUserDTO payload){

        if (usuarioRepository.findByUsername(payload.getUsername()).isPresent()){
            throw new CustomBadRequestException("El username ya esta en uso");
        }

        //Crear el registro de personas
        Persona nuevaPersona = new Persona();
        nuevaPersona.setNombre(payload.getNombres());
        nuevaPersona.setPrimerApellido(payload.getPrimerApellido());
        nuevaPersona.setSegundoApellido(payload.getSegundoApellido());
        nuevaPersona.setCorreo(payload.getCorreo());
        nuevaPersona.setCurp(payload.getCurp());
        nuevaPersona.setFechaNacimiento(payload.getFechaNacimiento());

        nuevaPersona = personaRepository.save(nuevaPersona);

        //Crear la instancia de usuario
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername(payload.getUsername());
        nuevoUsuario.setPassword(payload.getPassword());
        nuevoUsuario.setDescripcion(payload.getDescripcion());
        nuevoUsuario.setEnable(true);
        nuevoUsuario.setRol(
                Roles.valueOf(payload.getRol())
        );
        nuevoUsuario.setPersona(nuevaPersona);

        return usuarioRepository.save(nuevoUsuario);
    }


    //-------------------------------------------------------------------------------


    //Actualizar username y descripcion de usuario
    @Transactional
    public Usuario updateUser(Long id, RequestUpdatePersona payload) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new CustomBadRequestException("Usuario no encontrado"));
        usuario.setUsername(payload.getUsername());
        usuario.setDescripcion(payload.getDescripcion());

        return usuarioRepository.save(usuario);
    }

    //Activar o desactivar usuario
    @Transactional
    public Usuario toggleUserStatus(Long id) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new CustomBadRequestException("Usuario no encontrado"));
        usuarioExistente.setEnable(!usuarioExistente.isEnable());
        return usuarioRepository.save(usuarioExistente);
    }

    //Consultar informacion de usuario mediante id de usuario
    public Usuario getUserById(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new CustomBadRequestException("Usuario no encontrado")
        );
    }

    /*

     */


}