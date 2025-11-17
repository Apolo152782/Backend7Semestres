package com.ProyectoDeAula5.Proyecto5.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ProyectoDeAula5.Proyecto5.model.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByCorreo(String correo);

    Optional<Usuario> findByCorreoAndPass(String correo, String pass);

}
