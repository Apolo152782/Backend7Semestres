package com.ProyectoDeAula5.Proyecto5.controller;

import com.ProyectoDeAula5.Proyecto5.model.DetalleVenta;
import com.ProyectoDeAula5.Proyecto5.service.DetalleVentaService;
import com.ProyectoDeAula5.Proyecto5.service.VentaService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/detalleventa")
@CrossOrigin(origins = "http://localhost:3000")
public class DetalleVentaController {

    @Autowired
    private VentaService ventaService;

    @Autowired
    private DetalleVentaService detalleVentaService;

    @PostMapping("/detalle/guardar")
    public DetalleVenta guardarDetalleVenta(@RequestBody DetalleVenta detalleVenta) {
        return ventaService.guardarDetalleVenta(detalleVenta);
    }

    @GetMapping("/listar")
    public List<DetalleVenta> listarDetalleVenta() {
        return detalleVentaService.listarDetalleVenta();
    }
};