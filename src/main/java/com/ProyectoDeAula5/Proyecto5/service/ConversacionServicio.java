package com.ProyectoDeAula5.Proyecto5.service;

import com.ProyectoDeAula5.Proyecto5.model.Conversacion;
import com.ProyectoDeAula5.Proyecto5.model.Mensaje;
import com.ProyectoDeAula5.Proyecto5.repository.ConversacionRepositorio;
import com.ProyectoDeAula5.Proyecto5.repository.MensajeRepositorio;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Service
public class ConversacionServicio {

    private final ConversacionRepositorio conversacionRepositorio;
    private final MensajeRepositorio mensajeRepositorio;
    private final GeminiServicio geminiServicio;
    private final int maxMensajesHistorial;

    public ConversacionServicio(ConversacionRepositorio conversacionRepositorio,
            MensajeRepositorio mensajeRepositorio,
            GeminiServicio geminiServicio,
            @Value("${gemini.historial.maxMensajes:20}") int maxMensajesHistorial) {
        this.conversacionRepositorio = conversacionRepositorio;
        this.mensajeRepositorio = mensajeRepositorio;
        this.geminiServicio = geminiServicio;
        this.maxMensajesHistorial = maxMensajesHistorial;
    }

    @Transactional
    public Conversacion obtenerOCrear(Long conversacionId, String usuarioId, String primerMensaje) {
        if (conversacionId != null) {
            return conversacionRepositorio.findById(conversacionId)
                    .orElseThrow(() -> new IllegalArgumentException("Conversación no encontrada"));
        }
        Conversacion c = new Conversacion();
        c.setUsuarioId(usuarioId);
        c.setTitulo(generarTitulo(primerMensaje));
        return conversacionRepositorio.save(c);
    }

    private String generarTitulo(String texto) {
        if (texto == null || texto.isBlank())
            return "Conversación";
        String t = texto.strip();
        return t.length() > 50 ? t.substring(0, 47) + "..." : t;
    }

    @Transactional
    public String enviarYGuardar(Conversacion conversacion, String mensajeUsuario) {
        Mensaje mUsu = new Mensaje();
        mUsu.setConversacion(conversacion);
        mUsu.setRol(Mensaje.Rol.USUARIO);
        mUsu.setContenido(mensajeUsuario);
        mensajeRepositorio.save(mUsu);

        List<Map<String, Object>> contents = construirContenidoGemini(conversacion);
        String respuesta = geminiServicio.generarTextoConHistorial(contents);

        Mensaje mAsis = new Mensaje();
        mAsis.setConversacion(conversacion);
        mAsis.setRol(Mensaje.Rol.ASISTENTE);
        mAsis.setContenido(respuesta);
        mensajeRepositorio.save(mAsis);

        conversacion.setActualizadaEn(Instant.now());
        conversacionRepositorio.save(conversacion);

        return respuesta;
    }

    private List<Map<String, Object>> construirContenidoGemini(Conversacion conversacion) {
        List<Map<String, Object>> lista = new ArrayList<>();
        List<Mensaje> ultimosDesc = mensajeRepositorio.findTop200ByConversacionOrderByCreadoEnDesc(conversacion);
        Collections.reverse(ultimosDesc);
        if (ultimosDesc.size() > maxMensajesHistorial) {
            ultimosDesc = ultimosDesc.subList(ultimosDesc.size() - maxMensajesHistorial, ultimosDesc.size());
        }
        for (Mensaje msj : ultimosDesc) {
            String rolGemini = (msj.getRol() == Mensaje.Rol.USUARIO) ? "user" : "model";
            lista.add(new LinkedHashMap<String, Object>() {
                {
                    put("role", rolGemini);
                    put("parts", Collections.singletonList(Collections.singletonMap("text", msj.getContenido())));
                }
            });
        }
        return lista;
    }

    @Transactional(readOnly = true)
    public List<Conversacion> listarPorUsuario(String usuarioId) {
        return conversacionRepositorio.findByUsuarioIdOrderByActualizadaEnDesc(usuarioId);
    }

    @Transactional
    public com.ProyectoDeAula5.Proyecto5.model.Conversacion renombrar(Long id, String nuevoTitulo) {
        var c = conversacionRepositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Conversación no encontrada"));
        c.setTitulo(nuevoTitulo);
        c.setActualizadaEn(java.time.Instant.now());
        return conversacionRepositorio.save(c);
    }

    @Transactional(readOnly = true)
    public List<Mensaje> mensajesDe(Long conversacionId) {
        Conversacion c = conversacionRepositorio.findById(conversacionId)
                .orElseThrow(() -> new IllegalArgumentException("Conversación no encontrada"));
        // Devuelve el historial en orden cronológico ascendente (del más antiguo al más
        // nuevo)
        return mensajeRepositorio.findByConversacionOrderByCreadoEnAsc(c);
    }

    @Transactional
    public void eliminarConversacion(Long id) {
        Conversacion c = conversacionRepositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Conversación no encontrada"));
        // 1) Borrar mensajes hijos
        mensajeRepositorio.deleteByConversacion(c);
        // 2) Borrar conversación
        conversacionRepositorio.delete(c);
    }
}