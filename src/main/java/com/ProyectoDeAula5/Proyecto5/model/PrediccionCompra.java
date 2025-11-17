package com.ProyectoDeAula5.Proyecto5.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class PrediccionCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double edad;
    private double ingresos;
    private double comprasAnteriores;
    private String resultado;

    private double probabilidad; // <-- Nuevo campo agregado

}