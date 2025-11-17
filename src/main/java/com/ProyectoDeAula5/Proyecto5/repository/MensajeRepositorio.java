package com.ProyectoDeAula5.Proyecto5.repository;

import com.ProyectoDeAula5.Proyecto5.model.Conversacion;
import com.ProyectoDeAula5.Proyecto5.model.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MensajeRepositorio extends JpaRepository<Mensaje, Long> {
    List<Mensaje> findByConversacionOrderByCreadoEnAsc(Conversacion conversacion);

    List<Mensaje> findTop200ByConversacionOrderByCreadoEnDesc(Conversacion conversacion);

    // NUEVO: borrar todos los mensajes de una conversación
    void deleteByConversacion(Conversacion conversacion);

}