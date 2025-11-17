package com.ProyectoDeAula5.Proyecto5.controller;

import com.ProyectoDeAula5.Proyecto5.model.Cliente;
import com.ProyectoDeAula5.Proyecto5.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "http://localhost:3000")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public List<Cliente> listarClientes() {
        return clienteService.listarTodas();
    }

    @GetMapping("/{id}")
    public Cliente obtenerCliente(@PathVariable Long id) {
        return clienteService.obtenerPorId(id);
    }

    @PostMapping
    public Cliente guardarCliente(@RequestBody Cliente cliente) {
        return clienteService.guardar(cliente);
    }

    @PutMapping("/{id}")
    public Cliente editarCliente(@PathVariable Long id, @RequestBody Cliente clienteDetalles) {
        Cliente clienteExistente = clienteService.obtenerPorId(id);
        clienteExistente.setNombre(clienteDetalles.getNombre());

        clienteExistente.setDni(clienteDetalles.getDni());
        clienteExistente.setTelefono(clienteDetalles.getTelefono());
        clienteExistente.setEdad(clienteDetalles.getEdad());
        clienteExistente.setGenero(clienteDetalles.getGenero());
        clienteExistente.setDireccion(clienteDetalles.getDireccion());
        clienteExistente.setRazon(clienteDetalles.getRazon());
        return clienteService.guardar(clienteExistente);
    }

    @DeleteMapping("/{id}")
    public void eliminarCliente(@PathVariable Long id) {
        clienteService.eliminar(id);
    }

    // Cambia este endpoint existente
    @GetMapping("/dni/nombre/{dni}")
    public ResponseEntity<Cliente> obtenerClientePorDni(@PathVariable int dni) {
        Cliente cliente = clienteService.obtenerPorDni(dni);
        if (cliente != null) {
            return ResponseEntity.ok(cliente);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
