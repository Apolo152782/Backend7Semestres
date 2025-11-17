package com.ProyectoDeAula5.Proyecto5.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ProyectoDeAula5.Proyecto5.model.Producto;
import com.ProyectoDeAula5.Proyecto5.model.Proveedor;
import com.ProyectoDeAula5.Proyecto5.service.ProductoService;
import com.ProyectoDeAula5.Proyecto5.service.ProveedorService;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ProveedorService proveedorService;

    @GetMapping
    public ResponseEntity<List<Producto>> listarProductos() {
        List<Producto> productos = productoService.listarTodas();
        return new ResponseEntity<>(productos, HttpStatus.OK);
    }

    @GetMapping("/codigo/detalles/{codigo}")
    public ResponseEntity<String> obtenerDetallesProductoPorCodigo(@PathVariable int codigo) {
        Producto producto = productoService.obtenerPorCodigo(codigo);
        if (producto != null) {
            String detalles = "Nombre: " + producto.getNombre() +
                    ", Cantidad: " + producto.getStock() +
                    ", Precio: " + producto.getPrecio();
            return new ResponseEntity<>(detalles, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{codigo}")
    public Producto obtenerProductoPorCodigo(@PathVariable int codigo) {
        return productoService.obtenerPorCodigo(codigo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProducto(@PathVariable Long id) {
        Optional<Producto> producto = productoService.obtenerPorId(id);
        return producto.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<?> guardarProducto(@RequestBody Producto producto) {

        if (producto.getCodigo() <= 0 || producto.getNombre() == null || producto.getProveedor() == null
                || producto.getStock() < 0
                || producto.getPrecio() < 0) {
            return new ResponseEntity<>("Datos del producto inválidos", HttpStatus.BAD_REQUEST);
        }

        if (producto.getProveedor() == null || producto.getProveedor().getId() == null) {
            return new ResponseEntity<>("Proveedor no especificado", HttpStatus.BAD_REQUEST);
        }
        Proveedor proveedor = proveedorService.obtenerPorId(producto.getProveedor().getId());
        if (proveedor == null) {
            return new ResponseEntity<>("Proveedor no encontrado", HttpStatus.NOT_FOUND);
        }

        producto.setProveedor(proveedor);

        try {
            Producto productoGuardado = productoService.guardar(producto);
            return new ResponseEntity<>(productoGuardado, HttpStatus.CREATED);
        } catch (Exception e) {
            System.err.println("Error al guardar producto: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/nombre/detalles/{nombre}")
    public ResponseEntity<String> obtenerDetallesProductoPorNombre(@PathVariable String nombre) {
        Producto producto = productoService.obtenerPorNombre(nombre);
        if (producto != null) {
            String detalles = "Nombre: " + producto.getNombre() +
                    ", Cantidad: " + producto.getStock() +
                    ", Precio: " + producto.getPrecio() +
                    ", Codigo: " + producto.getCodigo();
            return new ResponseEntity<>(detalles, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editarProducto(@PathVariable Long id, @RequestBody Producto productoDetalles) {
        Optional<Producto> productoExistente = productoService.obtenerPorId(id);

        if (!productoExistente.isPresent()) {
            return new ResponseEntity<>("Producto no encontrado", HttpStatus.NOT_FOUND);
        }

        Producto producto = productoExistente.get();
        producto.setCodigo(productoDetalles.getCodigo());
        producto.setNombre(productoDetalles.getNombre());
        producto.setStock(productoDetalles.getStock());
        producto.setPrecio(productoDetalles.getPrecio());

        if (productoDetalles.getProveedor() != null && productoDetalles.getProveedor().getId() != null) {
            Proveedor proveedor = proveedorService.obtenerPorId(productoDetalles.getProveedor().getId());
            if (proveedor == null) {
                return new ResponseEntity<>("Proveedor no encontrado", HttpStatus.NOT_FOUND);
            }
            producto.setProveedor(proveedor);
        }

        Producto productoActualizado = productoService.guardar(producto);
        return new ResponseEntity<>(productoActualizado, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id) {
        Optional<Producto> productoExistente = productoService.obtenerPorId(id);

        if (!productoExistente.isPresent()) {
            return new ResponseEntity<>("Producto no encontrado", HttpStatus.NOT_FOUND);
        }

        try {
            productoService.eliminar(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            System.err.println("Error al eliminar producto: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Producto>> buscarPorNombre(@RequestParam String nombre) {
        List<Producto> productos = productoService.buscarPorNombreContaining(nombre);
        return new ResponseEntity<>(productos, HttpStatus.OK);
    }

}
