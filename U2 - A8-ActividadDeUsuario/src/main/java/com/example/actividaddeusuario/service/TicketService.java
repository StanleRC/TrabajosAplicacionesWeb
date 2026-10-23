package com.example.actividaddeusuario.service;

import com.example.actividaddeusuario.dto.CrearTicketDTO;
import com.example.actividaddeusuario.dto.ReasignarPrioridadDTO;
import com.example.actividaddeusuario.exceptions.RecursoNoEncontradoException;
import com.example.actividaddeusuario.exceptions.ReglaNegocioException;
import com.example.actividaddeusuario.model.EstadoTicket;
import com.example.actividaddeusuario.model.Ticket;
import com.example.actividaddeusuario.model.Usuario;
import com.example.actividaddeusuario.repository.TicketRepository;
import com.example.actividaddeusuario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public Ticket crearTicket(CrearTicketDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        if (!usuario.getActivo()) {
            throw new ReglaNegocioException("El usuario no está activo, no puede crear tickets");
        }

        Ticket ticket = new Ticket();
        ticket.setDescripcion(dto.getDescripcion());
        ticket.setPrioridad(dto.getPrioridad());
        ticket.setEstado(EstadoTicket.ABIERTO);
        ticket.setUsuario(usuario);

        return ticketRepository.save(ticket);
    }

    @Transactional
    public Ticket actualizarEstadoTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Ticket no encontrado"));

        if (ticket.getEstado() == EstadoTicket.CERRADO) {
            throw new ReglaNegocioException("El ticket ya está CERRADO y no se puede modificar");
        }

        if (ticket.getEstado() == EstadoTicket.ABIERTO) {
            ticket.setEstado(EstadoTicket.EN_PROCESO);
        } else if (ticket.getEstado() == EstadoTicket.EN_PROCESO) {
            ticket.setEstado(EstadoTicket.CERRADO);
        }

        return ticketRepository.save(ticket);
    }

    @Transactional
    public Ticket reasignarPrioridad(Long ticketId, ReasignarPrioridadDTO dto) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Ticket no encontrado"));

        if (ticket.getEstado() == EstadoTicket.CERRADO) {
            throw new ReglaNegocioException("No se puede cambiar la prioridad de un ticket CERRADO");
        }

        ticket.setPrioridad(dto.getPrioridad());
        return ticketRepository.save(ticket);
    }

    @Transactional
    public Usuario desactivarUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        usuario.setActivo(false);
        return usuarioRepository.save(usuario);
    }

    public List<Ticket> obtenerTicketsUsuario(Long usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new RecursoNoEncontradoException("Usuario no encontrado");
        }
        return ticketRepository.findByUsuarioId(usuarioId);
    }
}
