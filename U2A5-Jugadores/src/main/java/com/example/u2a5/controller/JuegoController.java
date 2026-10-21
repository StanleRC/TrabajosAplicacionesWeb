package com.example.u2a5.controller;

import com.example.u2a5.dto.IniciarPartidaDTO;
import com.example.u2a5.dto.RecargarSaldoDTO;
import com.example.u2a5.model.Jugador;
import com.example.u2a5.model.Partida;
import com.example.u2a5.model.Tiro;
import com.example.u2a5.service.JuegoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/juego")
public class JuegoController {

    @Autowired
    private JuegoService juegoService;

    @PostMapping("/partida/iniciar")
    public ResponseEntity<Partida> iniciarPartida(@Valid @RequestBody IniciarPartidaDTO dto) {
        return new ResponseEntity<>(juegoService.iniciarPartida(dto), HttpStatus.CREATED);
    }

    @PostMapping("/partida/{partidaId}/tiro")
    public ResponseEntity<Tiro> realizarTiro(@PathVariable Long partidaId) {
        return new ResponseEntity<>(juegoService.realizarTiro(partidaId), HttpStatus.OK);
    }

    @PutMapping("/partida/{partidaId}/finalizar")
    public ResponseEntity<Partida> finalizarPartidaManual(@PathVariable Long partidaId) {
        return new ResponseEntity<>(juegoService.finalizarPartidaManual(partidaId), HttpStatus.OK);
    }

    @PutMapping("/jugador/{jugadorId}/recargar")
    public ResponseEntity<Jugador> recargarSaldo(
            @PathVariable Long jugadorId,
            @Valid @RequestBody RecargarSaldoDTO dto) {
        return new ResponseEntity<>(juegoService.recargarSaldo(jugadorId, dto.getMonto()), HttpStatus.OK);
    }

    @GetMapping("/jugador/{jugadorId}/historial")
    public ResponseEntity<List<Partida>> obtenerHistorialPartida(@PathVariable Long jugadorId) {
        return new ResponseEntity<>(juegoService.obtenerHistorialPartida(jugadorId), HttpStatus.OK);
    }
}