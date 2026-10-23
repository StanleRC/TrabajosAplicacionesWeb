package com.example.actividaddeusuario.controller;

import com.example.actividaddeusuario.dto.CrearTicketDTO;
import com.example.actividaddeusuario.dto.ReasignarPrioridadDTO;
import com.example.actividaddeusuario.model.Ticket;
import com.example.actividaddeusuario.model.Usuario;
import com.example.actividaddeusuario.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping("/tickets")
    public ResponseEntity<Ticket> crearTicket(@Valid @RequestBody CrearTicketDTO dto) {
        return new ResponseEntity<>(ticketService.crearTicket(dto), HttpStatus.CREATED);
    }

    @PutMapping("/tickets/{id}/estado")
    public ResponseEntity<Ticket> actualizarEstadoTicket(@PathVariable Long id) {
        return new ResponseEntity<>(ticketService.actualizarEstadoTicket(id), HttpStatus.OK);
    }

    @PutMapping("/tickets/{id}/prioridad")
    public ResponseEntity<Ticket> reasignarPrioridad(
            @PathVariable Long id,
            @Valid @RequestBody ReasignarPrioridadDTO dto) {
        return new ResponseEntity<>(ticketService.reasignarPrioridad(id, dto), HttpStatus.OK);
    }

    @PutMapping("/usuarios/{id}/desactivar")
    public ResponseEntity<Usuario> desactivarUsuario(@PathVariable Long id) {
        return new ResponseEntity<>(ticketService.desactivarUsuario(id), HttpStatus.OK);
    }

    @GetMapping("/usuarios/{id}/tickets")
    public ResponseEntity<List<Ticket>> obtenerTicketsUsuario(@PathVariable Long id) {
        return new ResponseEntity<>(ticketService.obtenerTicketsUsuario(id), HttpStatus.OK);
    }
}