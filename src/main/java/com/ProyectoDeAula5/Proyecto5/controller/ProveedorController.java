package com.ProyectoDeAula5.Proyecto5.controller;

import com.ProyectoDeAula5.Proyecto5.model.Proveedor;
import com.ProyectoDeAula5.Proyecto5.service.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
@CrossOrigin(origins = "https://frontend7-semestre.vercel.app")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    @GetMapping
    public List<Proveedor> listarProveedores() {
        return proveedorService.listarTodas();
    }

    @GetMapping("/{id}")
    public Proveedor obtenerProveedor(@PathVariable Long id) {
        return proveedorService.obtenerPorId(id);
    }

    @PostMapping
    public Proveedor guardarProveedor(@RequestBody Proveedor proveedor) {
        return proveedorService.guardar(proveedor);
    }

    @PutMapping("/{id}")
    public Proveedor editarProveedor(@PathVariable Long id, @RequestBody Proveedor proveedorDetalles) {
        Proveedor proveedorExistente = proveedorService.obtenerPorId(id);
        proveedorExistente.setNombre(proveedorDetalles.getNombre());
        proveedorExistente.setRuc(proveedorDetalles.getRuc());
        proveedorExistente.setTelefono(proveedorDetalles.getTelefono());
        proveedorExistente.setDireccion(proveedorDetalles.getDireccion());
        proveedorExistente.setRazon(proveedorDetalles.getRazon());
        return proveedorService.guardar(proveedorExistente);
    }

    @DeleteMapping("/{id}")
    public void eliminarProveedor(@PathVariable Long id) {
        proveedorService.eliminar(id);
    }
}
