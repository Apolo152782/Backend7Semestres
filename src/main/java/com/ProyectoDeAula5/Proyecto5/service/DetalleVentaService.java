package com.ProyectoDeAula5.Proyecto5.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.ProyectoDeAula5.Proyecto5.model.DetalleVenta;
import com.ProyectoDeAula5.Proyecto5.repository.DetalleVentaRepository;

@Service
public class DetalleVentaService {
    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    public List<DetalleVenta> listarDetalleVenta() {
        try {
            return detalleVentaRepository.findAll();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al obtener los detalles de venta", e);
        }
    }

};
