package com.utez.biblioteca.repository;

import com.utez.biblioteca.model.EstadoPrestamo;
import com.utez.biblioteca.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
    int countByLectorIdAndEstadoIn(Long lectorId, List<EstadoPrestamo> estados);

    List<Prestamo> findByLectorIdAndEstadoIn(Long lectorId, List<EstadoPrestamo> estados);
}