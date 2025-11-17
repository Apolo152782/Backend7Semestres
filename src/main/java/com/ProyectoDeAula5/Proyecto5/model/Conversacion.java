package com.ProyectoDeAula5.Proyecto5.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.Instant;

@Data
@Entity
@Table(name = "conversaciones")
public class Conversacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String usuarioId;

    @Column(length = 120)
    private String titulo;

    @Column(nullable = false)
    private Instant creadaEn;

    @Column(nullable = false)
    private Instant actualizadaEn;

    @PrePersist
    void alCrear() {
        Instant ahora = Instant.now();
        creadaEn = ahora;
        actualizadaEn = ahora;
    }

    @PreUpdate
    void alActualizar() {
        actualizadaEn = Instant.now();
    }
}