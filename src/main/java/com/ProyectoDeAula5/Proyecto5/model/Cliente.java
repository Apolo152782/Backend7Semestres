package com.ProyectoDeAula5.Proyecto5.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int dni;
    private String nombre;
    private String telefono;
    private String direccion;
    private String razon;
    private int edad;
    private String genero;
    private Double satisfaccionPromedio;

}