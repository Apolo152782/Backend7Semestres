package com.ProyectoDeAula5.Proyecto5.service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrediccionCompraDTO {

    private String genero;
    private double frecuenciaCompra;
    private double montoPromedio;
    private double ultimaCompra;
    private String descuentoRecibido;
    private String metodoPago;
    private Double satisfaccion;

}
