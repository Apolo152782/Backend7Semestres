package com.ProyectoDeAula5.Proyecto5.service;

import com.ProyectoDeAula5.Proyecto5.model.Producto;
import com.ProyectoDeAula5.Proyecto5.repository.ProductoRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> listarTodas() {
        return productoRepository.findAll();
    }

    public Optional<Producto> obtenerPorId(Long id) {
        return productoRepository.findById(id);
    }

    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }

    public void eliminar(Long id) {
        productoRepository.deleteById(id);
    }

    public void actualizarInventario(int codigo, int cantidad) {
        log.info("Actualizando productos");
        Producto producto = productoRepository.findByCodigo(codigo);
        if (producto != null) {
            int nuevoStock = producto.getStock() - cantidad;
            producto.setStock(Math.max(0, nuevoStock)); // Evita valores negativos
            productoRepository.save(producto);
            log.info("Stock actualizado para el producto " + producto.getNombre() + ": " + producto.getStock());
        } else {
            log.warn("Producto con código " + codigo + " no encontrado.");
        }
    }

    public Producto obtenerPorCodigo(int codigo) {
        return productoRepository.findByCodigo(codigo);
    }

    public Producto obtenerPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre)
                .stream()
                .findFirst()
                .orElse(null);
    }

    public List<Producto> buscarPorNombreContaining(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }
}
