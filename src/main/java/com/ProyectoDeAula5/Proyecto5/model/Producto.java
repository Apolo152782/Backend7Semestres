package com.ProyectoDeAula5.Proyecto5.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "productos")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int codigo;
    private String nombre;
    private int stock;
    private double precio;

    @ManyToOne // Relacion con Proveedor
    @JoinColumn(name = "proveedor_id", nullable = true)
    private Proveedor proveedor;

    private String proveedorNombre;
}