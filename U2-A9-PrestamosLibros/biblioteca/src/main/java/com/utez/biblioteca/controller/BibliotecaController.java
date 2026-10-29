package com.utez.biblioteca.controller;

import com.utez.biblioteca.dto.SolicitarPrestamoDTO;
import com.utez.biblioteca.model.Lector;
import com.utez.biblioteca.model.Prestamo;
import com.utez.biblioteca.service.PrestamoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BibliotecaController {

    @Autowired
    private PrestamoService prestamoService;

    @PostMapping("/prestamos")
    public ResponseEntity<Prestamo> solicitarPrestamo(@Valid @RequestBody SolicitarPrestamoDTO dto) {
        return new ResponseEntity<>(prestamoService.solicitarPrestamo(dto), HttpStatus.CREATED);
    }

    @PutMapping("/prestamos/{id}/devolver")
    public ResponseEntity<Prestamo> devolverLibro(@PathVariable Long id) {
        return new ResponseEntity<>(prestamoService.devolverLibro(id), HttpStatus.OK);
    }

    @PutMapping("/prestamos/{id}/renovar")
    public ResponseEntity<Prestamo> renovarPrestamo(@PathVariable Long id) {
        return new ResponseEntity<>(prestamoService.renovarPrestamo(id), HttpStatus.OK);
    }

    @PutMapping("/lectores/{id}/rehabilitar")
    public ResponseEntity<Lector> rehabilitarLector(@PathVariable Long id) {
        return new ResponseEntity<>(prestamoService.rehabilitarLector(id), HttpStatus.OK);
    }

    @GetMapping("/lectores/{id}/prestamos-activos")
    public ResponseEntity<List<Prestamo>> listarPrestamosActivosLector(@PathVariable Long id) {
        return new ResponseEntity<>(prestamoService.listarPrestamosActivosLector(id), HttpStatus.OK);
    }
}