package com.ProyectoDeAula5.Proyecto5.controller;

import com.ProyectoDeAula5.Proyecto5.model.Venta;
import com.ProyectoDeAula5.Proyecto5.repository.VentaRepository; // Asegúrate de importar el repositorio
import com.ProyectoDeAula5.Proyecto5.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ventas")
@CrossOrigin(origins = "http://localhost:3000")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @Autowired
    private VentaRepository ventaRepository; // Inyecta el repositorio correctamente

    // Endpoint para listar ventas
    @GetMapping("/listar")
    public List<Venta> listarVentas() {
        return ventaService.obtenerVentas();
    }

    // Endpoint para guardar una venta
    @PostMapping("/guardar")
    public ResponseEntity<Venta> guardarVenta(@RequestBody Venta venta) {
        System.out.println("Datos recibidos: " + venta.toString());
        venta.getDetallesVenta().forEach(d -> {
            System.out.println("Detalle - SatisfactionScore: " + d.getSatisfactionScore());
        });
        return ResponseEntity.ok(ventaService.guardarVenta(venta));
    }

    // Endpoint para obtener totales por cliente
    @GetMapping("/totales-clientes")
    public List<Object[]> obtenerTotalesPorCliente() {
        return ventaRepository.calcularMontoTotalPorCliente(); // Usa el repositorio correctamente
    }
}
