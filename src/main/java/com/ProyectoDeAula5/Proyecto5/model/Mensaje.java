package com.ProyectoDeAula5.Proyecto5.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.Instant;

@Data
@Entity
@Table(name = "mensajes")
public class Mensaje {

    public enum Rol {
        USUARIO, ASISTENTE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Conversacion conversacion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private Rol rol;

    // Usa LONGTEXT explícitamente (para evitar depender de @Lob ambiguo)
    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String contenido;

    @Column(nullable = false)
    private Instant creadoEn;

    @PrePersist
    void alCrear() {
        creadoEn = Instant.now();
    }
}