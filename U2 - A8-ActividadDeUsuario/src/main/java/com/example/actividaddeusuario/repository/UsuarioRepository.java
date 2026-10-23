package com.example.actividaddeusuario.repository;

import com.example.actividaddeusuario.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}