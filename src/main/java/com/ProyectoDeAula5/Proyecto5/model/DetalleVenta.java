package com.ProyectoDeAula5.Proyecto5.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@ToString
@Table(name = "detalles")
public class DetalleVenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // Cambiado a Integer para mejor compatibilidad

    private double impuesto;
    private int cod_pro;
    private String nompro;
    private int cantidad;
    private double precio;
    private String fecha;

    @Column(name = "satisfaction_score")
    private Double satisfactionScore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venta_id", referencedColumnName = "id")
    private Venta venta;
}