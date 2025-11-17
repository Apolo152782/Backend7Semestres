
package com.ProyectoDeAula5.Proyecto5.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ProyectoDeAula5.Proyecto5.repository.DetallesRepository;
import com.ProyectoDeAula5.Proyecto5.repository.VentaRepository;

import java.util.List;

@RestController
@RequestMapping("/api/graficos")
@CrossOrigin(origins = "http://localhost:3000")
public class GraficoController {

    @Autowired
    private DetallesRepository detallesRepository;

    @Autowired // ¡Falta esta anotación!
    private VentaRepository ventaRepository; // Debes declarar la propiedad

    @GetMapping("/ventas-mensuales")
    public List<Object[]> getVentasMensuales() {
        return ventaRepository.findVentasAgrupadasPorMes(); // Usa la instancia inyectada
    }

    @GetMapping("/top-productos")
    public List<Object[]> getTopProductos() {
        return detallesRepository.findTopProductos();
    }
}
