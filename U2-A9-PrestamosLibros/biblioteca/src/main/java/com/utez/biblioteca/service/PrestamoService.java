package com.utez.biblioteca.service;

import com.utez.biblioteca.dto.SolicitarPrestamoDTO;
import com.utez.biblioteca.exception.RecursoNoEncontradoException;
import com.utez.biblioteca.exception.ReglaNegocioException;
import com.utez.biblioteca.model.*;
import com.utez.biblioteca.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
public class PrestamoService {

    @Autowired private LectorRepository lectorRepository;
    @Autowired private LibroRepository libroRepository;
    @Autowired private PrestamoRepository prestamoRepository;

    private final List<EstadoPrestamo> ESTADOS_ACTIVOS = Arrays.asList(EstadoPrestamo.PRESTADO, EstadoPrestamo.VENCIDO);

    @Transactional
    public Prestamo solicitarPrestamo(SolicitarPrestamoDTO dto) {
        Lector lector = lectorRepository.findById(dto.getLectorId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Lector no encontrado"));

        if (!lector.getActivo()) {
            throw new ReglaNegocioException("El lector no está activo y no puede solicitar préstamos");
        }

        Libro libro = libroRepository.findById(dto.getLibroId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Libro no encontrado"));

        if (libro.getEjemplaresDisponibles() < 1) {
            throw new ReglaNegocioException("No hay ejemplares disponibles de este libro");
        }

        int prestamosActivos = prestamoRepository.countByLectorIdAndEstadoIn(lector.getId(), ESTADOS_ACTIVOS);
        int limite = lector.getMembresiaPremium() ? 5 : 3;

        if (prestamosActivos >= limite) {
            throw new ReglaNegocioException("El lector ha alcanzado su límite máximo de préstamos activos (" + limite + ")");
        }

        libro.setEjemplaresDisponibles(libro.getEjemplaresDisponibles() - 1);
        libroRepository.save(libro);

        Prestamo prestamo = new Prestamo();
        prestamo.setLector(lector);
        prestamo.setLibro(libro);
        prestamo.setFechaPrestamo(LocalDate.now());
        prestamo.setFechaDevolucionMaxima(LocalDate.now().plusDays(14));
        prestamo.setEstado(EstadoPrestamo.PRESTADO);

        return prestamoRepository.save(prestamo);
    }

    @Transactional
    public Prestamo devolverLibro(Long idPrestamo) {
        Prestamo prestamo = prestamoRepository.findById(idPrestamo)
                .orElseThrow(() -> new RecursoNoEncontradoException("Préstamo no encontrado"));

        if (prestamo.getEstado() == EstadoPrestamo.DEVUELTO) {
            throw new ReglaNegocioException("Este libro ya fue devuelto");
        }

        prestamo.setEstado(EstadoPrestamo.DEVUELTO);

        Libro libro = prestamo.getLibro();
        libro.setEjemplaresDisponibles(libro.getEjemplaresDisponibles() + 1);
        libroRepository.save(libro);

        // Si la entrega fue tardía, bloquear al lector
        if (LocalDate.now().isAfter(prestamo.getFechaDevolucionMaxima())) {
            Lector lector = prestamo.getLector();
            lector.setActivo(false);
            lectorRepository.save(lector);
        }

        return prestamoRepository.save(prestamo);
    }

    @Transactional
    public Prestamo renovarPrestamo(Long idPrestamo) {
        Prestamo prestamo = prestamoRepository.findById(idPrestamo)
                .orElseThrow(() -> new RecursoNoEncontradoException("Préstamo no encontrado"));

        if (prestamo.getEstado() != EstadoPrestamo.PRESTADO) {
            throw new ReglaNegocioException("No se puede renovar un préstamo que está " + prestamo.getEstado());
        }

        if (LocalDate.now().isAfter(prestamo.getFechaDevolucionMaxima())) {
            prestamo.setEstado(EstadoPrestamo.VENCIDO);
            prestamoRepository.save(prestamo);
            throw new ReglaNegocioException("No se puede renovar un préstamo ya vencido");
        }

        prestamo.setFechaDevolucionMaxima(prestamo.getFechaDevolucionMaxima().plusDays(7));
        return prestamoRepository.save(prestamo);
    }

    @Transactional
    public Lector rehabilitarLector(Long idLector) {
        Lector lector = lectorRepository.findById(idLector)
                .orElseThrow(() -> new RecursoNoEncontradoException("Lector no encontrado"));

        lector.setActivo(true);
        return lectorRepository.save(lector);
    }

    public List<Prestamo> listarPrestamosActivosLector(Long idLector) {
        if (!lectorRepository.existsById(idLector)) {
            throw new RecursoNoEncontradoException("Lector no encontrado");
        }
        return prestamoRepository.findByLectorIdAndEstadoIn(idLector, ESTADOS_ACTIVOS);
    }
}