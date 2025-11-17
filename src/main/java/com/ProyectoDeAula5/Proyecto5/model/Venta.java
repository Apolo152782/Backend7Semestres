package com.ProyectoDeAula5.Proyecto5.model;

import java.util.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity

@Table(name = "venta")
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nomcliente;
    private String codcliente;
    private String codempleado;
    @Column(name = "nombre_empleado") // para que cree la colunma en la base de datos
    private String nombreEmpleado;
    private double subtotal;
    private double total;
    private String fecha;
    private String metodoPago;

    @OneToMany
    private List<DetalleVenta> detallesVenta;

    @ManyToOne
    @JoinColumn(name = "cliente_id") // FK hacia cliente
    private Cliente cliente;
}