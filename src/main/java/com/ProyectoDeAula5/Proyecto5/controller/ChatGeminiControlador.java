package com.ProyectoDeAula5.Proyecto5.controller;

import com.ProyectoDeAula5.Proyecto5.model.Conversacion;
import com.ProyectoDeAula5.Proyecto5.model.Mensaje;
import com.ProyectoDeAula5.Proyecto5.service.ConversacionServicio;
import com.ProyectoDeAula5.Proyecto5.service.dto.ChatPeticion;
import com.ProyectoDeAula5.Proyecto5.service.dto.ChatRespuesta;

import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/gemini/chat")
@CrossOrigin(origins = "http://localhost:3000")
public class ChatGeminiControlador {

    private final ConversacionServicio conversacionServicio;

    public ChatGeminiControlador(ConversacionServicio conversacionServicio) {
        this.conversacionServicio = conversacionServicio;
    }

    @PostMapping("/send")
    public ChatRespuesta enviar(@RequestBody ChatPeticion peticion) {
        boolean nueva = (peticion.getConversacionId() == null);
        Conversacion c = conversacionServicio.obtenerOCrear(
                peticion.getConversacionId(),
                peticion.getUsuarioId(),
                peticion.getMensaje());
        String respuesta = conversacionServicio.enviarYGuardar(c, peticion.getMensaje());
        return new ChatRespuesta(c.getId(), respuesta, nueva);
    }

    // (Opcional) ver conversaciones del usuario si algún día lo quieres
    @GetMapping("/conversations")
    public List<Map<String, Object>> listarConversaciones(@RequestParam String userId) {
        return conversacionServicio.listarPorUsuario(userId)
                .stream()
                .map(c -> {
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("id", c.getId());
                    map.put("titulo", c.getTitulo());
                    map.put("actualizadaEn", c.getActualizadaEn());
                    return map;
                })
                .collect(Collectors.toList());
    }

    // ...
    @GetMapping("/conversations/{id}/messages")
    public List<Map<String, Object>> obtenerMensajes(@PathVariable("id") Long id) {
        List<Mensaje> lista = conversacionServicio.mensajesDe(id);
        List<Map<String, Object>> out = new java.util.ArrayList<>();
        for (Mensaje m : lista) {
            java.util.Map<String, Object> map = new java.util.LinkedHashMap<>();
            map.put("id", m.getId());
            map.put("rol", m.getRol().name());
            map.put("contenido", m.getContenido());
            map.put("creadoEn", m.getCreadoEn());
            out.add(map);
        }
        return out;
    }

    @DeleteMapping("/conversations/{id}")
    public Map<String, Object> eliminar(@PathVariable("id") Long id) {
        conversacionServicio.eliminarConversacion(id);
        java.util.Map<String, Object> map = new java.util.LinkedHashMap<>();
        map.put("eliminada", true);
        return map;
    }

    @PatchMapping("/conversations/{id}")
    public Map<String, Object> renombrar(@PathVariable("id") Long id,
            @RequestBody Map<String, String> body) {
        String nuevoTitulo = body.getOrDefault("titulo", "Conversación");
        com.ProyectoDeAula5.Proyecto5.model.Conversacion c = conversacionServicio.renombrar(id, nuevoTitulo);

        Map<String, Object> map = new java.util.LinkedHashMap<>();
        map.put("id", c.getId());
        map.put("titulo", c.getTitulo());
        return map;
    }
}