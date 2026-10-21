package com.example.u2a5.service;

import com.example.u2a5.dto.IniciarPartidaDTO;
import com.example.u2a5.enums.EstadoPartida;
import com.example.u2a5.exeption.RecursoNoEncontradoException;
import com.example.u2a5.exeption.ReglaNegocioException;
import com.example.u2a5.model.Jugador;
import com.example.u2a5.model.Partida;
import com.example.u2a5.model.Tiro;
import com.example.u2a5.repository.JugadorRepository;
import com.example.u2a5.repository.PartidaRepository;
import com.example.u2a5.repository.TiroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class JuegoService {

    @Autowired private JugadorRepository jugadorRepository;
    @Autowired private PartidaRepository partidaRepository;
    @Autowired private TiroRepository tiroRepository;

    @Transactional
    public Partida iniciarPartida(IniciarPartidaDTO dto) {
        Jugador jugador = jugadorRepository.findById(dto.getJugadorId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Jugador no encontrado"));

        if (!jugador.getActivo()) {
            throw new ReglaNegocioException("El jugador no está activo");
        }
        if (jugador.getSaldo() < dto.getApuesta()) {
            throw new ReglaNegocioException("Saldo insuficiente");
        }

        jugador.setSaldo(jugador.getSaldo() - dto.getApuesta());
        jugadorRepository.save(jugador);

        Partida partida = new Partida();
        partida.setFecha(LocalDateTime.now());
        partida.setEstado(EstadoPartida.EN_JUEGO);
        partida.setJugador(jugador);
        partida.setApuesta(dto.getApuesta());

        return partidaRepository.save(partida);
    }

    @Transactional
    public Tiro realizarTiro(Long partidaId) {
        Partida partida = partidaRepository.findById(partidaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Partida no encontrada"));

        if (partida.getEstado() != EstadoPartida.EN_JUEGO) {
            throw new ReglaNegocioException("La partida no está en juego");
        }

        Random random = new Random();
        int dado1 = random.nextInt(6) + 1;
        int dado2 = random.nextInt(6) + 1;
        int suma = dado1 + dado2;

        Tiro tiro = new Tiro();
        tiro.setPartida(partida);
        tiro.setValorDado1(dado1);
        tiro.setValorDado2(dado2);
        tiro.setSuma(suma);

        if (suma == 7 || suma == 11) {
            tiro.setEsGanador(true);
            partida.setEstado(EstadoPartida.FINALIZADA);
            Jugador jugador = partida.getJugador();
            jugador.setSaldo(jugador.getSaldo() + (partida.getApuesta() * 2));
            jugadorRepository.save(jugador);
        } else if (suma == 2 || suma == 3 || suma == 12) {
            tiro.setEsGanador(false);
            partida.setEstado(EstadoPartida.FINALIZADA);
        } else {
            tiro.setEsGanador(false); // Sigue jugando
        }

        partidaRepository.save(partida);
        return tiroRepository.save(tiro);
    }

    @Transactional
    public Partida finalizarPartidaManual(Long partidaId) {
        Partida partida = partidaRepository.findById(partidaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Partida no encontrada"));

        if (partida.getEstado() == EstadoPartida.FINALIZADA) {
            throw new ReglaNegocioException("La partida ya está finalizada");
        }

        partida.setEstado(EstadoPartida.FINALIZADA);
        return partidaRepository.save(partida);
    }

    @Transactional
    public Jugador recargarSaldo(Long jugadorId, Double monto) {
        Jugador jugador = jugadorRepository.findById(jugadorId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Jugador no encontrado"));

        jugador.setSaldo(jugador.getSaldo() + monto);
        return jugadorRepository.save(jugador);
    }

    public List<Partida> obtenerHistorialPartida(Long jugadorId) {
        if (!jugadorRepository.existsById(jugadorId)) {
            throw new RecursoNoEncontradoException("Jugador no encontrado");
        }
        return partidaRepository.findByJugadorId(jugadorId);
    }
}
