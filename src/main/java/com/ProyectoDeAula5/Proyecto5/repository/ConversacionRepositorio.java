package com.ProyectoDeAula5.Proyecto5.repository;

import com.ProyectoDeAula5.Proyecto5.model.Conversacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConversacionRepositorio extends JpaRepository<Conversacion, Long> {
    List<Conversacion> findByUsuarioIdOrderByActualizadaEnDesc(String usuarioId);
}