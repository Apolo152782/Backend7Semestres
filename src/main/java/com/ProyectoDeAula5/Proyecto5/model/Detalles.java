package com.ProyectoDeAula5.Proyecto5.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "detalles")
public class Detalles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private int cantidad;
    private int cod_pro;
    private String fecha;
    private String nompro;
    private double precio;
    private double impuesto;

}