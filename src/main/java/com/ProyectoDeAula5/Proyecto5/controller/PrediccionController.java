package com.ProyectoDeAula5.Proyecto5.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ProyectoDeAula5.Proyecto5.service.dto.PrediccionCompraDTO;
import com.ProyectoDeAula5.Proyecto5.service.dto.PrediccionService;

@RestController
@RequestMapping("/api/predecir")
@CrossOrigin(origins = "http://localhost:3000")
public class PrediccionController {

    @Autowired
    private PrediccionService servicio;

    @PostMapping
    public ResponseEntity<?> predecir(@RequestBody PrediccionCompraDTO datos) {
        try {
            String resultado = servicio.predecirCompra(datos);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al hacer la predicción: " + e.getMessage());
        }
    }

    @GetMapping("/dni/{dni}")
    public ResponseEntity<?> predecirPorDni(@PathVariable int dni) {
        try {
            Map<String, Object> resultado = servicio.predecirPorDni(dni);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }

}
