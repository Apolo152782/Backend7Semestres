package com.ProyectoDeAula5.Proyecto5.controller;

import com.ProyectoDeAula5.Proyecto5.model.Cliente;
import com.ProyectoDeAula5.Proyecto5.repository.ClienteRepository;
import com.ProyectoDeAula5.Proyecto5.service.EmailService;
import java.util.Optional;

import com.ProyectoDeAula5.Proyecto5.model.Venta;
import com.ProyectoDeAula5.Proyecto5.repository.VentaRepository; // Asegúrate de importar el repositorio
import com.ProyectoDeAula5.Proyecto5.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ventas")
@CrossOrigin(origins = "https://frontend7-semestre.vercel.app")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @Autowired
    private VentaRepository ventaRepository; // Inyecta el repositorio correctamente
    
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EmailService emailService;

    // Endpoint para listar ventas
    @GetMapping("/listar")
    public List<Venta> listarVentas() {
        return ventaService.obtenerVentas();
    }

    // Endpoint para guardar una venta
   @PostMapping("/guardar")
   public ResponseEntity<Venta>
   guardarVenta(
        @RequestBody Venta venta
    ) {

    System.out.println(
            "Datos recibidos: "
                    + venta.toString()
    );

    venta.getDetallesVenta()
            .forEach(d -> {

        System.out.println(
                "Detalle - SatisfactionScore: "
                        + d.getSatisfactionScore()
        );
    });

    Venta ventaGuardada =
            ventaService.guardarVenta(
                    venta
            );

    try {

        Optional<Cliente> clienteOptional =
                clienteRepository.findByDni(

                        Long.valueOf(
                                venta.getCodcliente()
                        )
                );

        if (clienteOptional.isPresent()) {

            Cliente cliente =
                    clienteOptional.get();

            if (
                    cliente.getCorreo()
                            != null
            ) {

          emailService.enviarFactura(
        
                cliente.getCorreo(),
        
                venta.getNomcliente(),
        
                venta.getNombreEmpleado(),
        
                venta.getSubtotal(),
        
                venta.getTotal(),
        
                venta.getMetodoPago(),
        
                venta.getFecha(),
        
                venta.getDetallesVenta()
                        );
                }
        }

    } catch (Exception e) {

        System.out.println(
                "Error enviando correo"
        );

        e.printStackTrace();
    }

    return ResponseEntity.ok(
            ventaGuardada
    );
}
    // Endpoint para obtener totales por cliente
    @GetMapping("/totales-clientes")
    public List<Object[]> obtenerTotalesPorCliente() {
        return ventaRepository.calcularMontoTotalPorCliente(); // Usa el repositorio correctamente
    }
}
