package com.ProyectoDeAula5.Proyecto5.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ProyectoDeAula5.Proyecto5.model.Producto;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // metodo para encontrar un producto por su codigo
    Producto findByCodigo(int codigo);

    // metodo para encontrar productos por el ID del proveedor
    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    // metodo para encontrar productos por el ID del proveedor
    List<Producto> findByProveedorId(Long proveedorId);
}
