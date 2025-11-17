package com.ProyectoDeAula5.Proyecto5.service;

import com.ProyectoDeAula5.Proyecto5.model.Proveedor;
import com.ProyectoDeAula5.Proyecto5.model.Producto;
import com.ProyectoDeAula5.Proyecto5.repository.ProveedorRepository;
import com.ProyectoDeAula5.Proyecto5.repository.ProductoRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProveedorService {

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public List<Proveedor> listarTodas() {
        return proveedorRepository.findAll();
    }

    public Proveedor obtenerPorId(Long id) {
        return proveedorRepository.findById(id).orElse(null);
    }

    public Proveedor guardar(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    public void eliminar(Long id) {

        List<Producto> productosAsociados = productoRepository.findByProveedorId(id);

        // Establecer el proveedor como null en los productos asociados
        for (Producto producto : productosAsociados) {
            producto.setProveedor(null);
            productoRepository.save(producto); // Guarda los cambios en el producto
        }

        proveedorRepository.deleteById(id);
    }
}
